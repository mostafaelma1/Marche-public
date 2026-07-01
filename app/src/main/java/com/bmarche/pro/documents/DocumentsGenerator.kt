package com.bmarche.pro.documents

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bmarche.pro.data.model.AppelOffre
import com.bmarche.pro.data.repository.MaSociete
import com.bmarche.pro.ui.Format
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/** Documents administratifs pré-remplis proposés par l'application. */
enum class TypeDocument(val titre: String, val slug: String) {
    ACTE_ENGAGEMENT("Acte d'engagement", "acte_engagement"),
    DECLARATION_HONNEUR("Déclaration sur l'honneur", "declaration_honneur"),
    LETTRE_MAINTIEN("Lettre de maintien de l'offre", "lettre_maintien_offre")
}

/** Paramètres saisis par l'utilisateur au moment de générer les documents. */
data class ParamsDocument(
    val montantOffre: String = "",
    val montantEnLettres: String = "",
    val delaiValiditeJours: Int = 90,
    val lieu: String = ""
)

object DocumentsGenerator {

    fun telecharger(
        context: Context, type: TypeDocument, ao: AppelOffre, societe: MaSociete, params: ParamsDocument
    ) {
        try {
            val f = genererPdf(context, type, ao, societe, params)
            enregistrer(context, f, "application/pdf")
            Toast.makeText(context, "Document téléchargé : ${f.name}", Toast.LENGTH_LONG).show()
            ouvrir(context, f, "application/pdf")
        } catch (e: Exception) {
            Toast.makeText(context, "Échec de la génération du document", Toast.LENGTH_SHORT).show()
        }
    }

    fun telechargerTout(
        context: Context, ao: AppelOffre, societe: MaSociete, params: ParamsDocument
    ) {
        try {
            val pdfs = TypeDocument.entries.map { genererPdf(context, it, ao, societe, params) }
            val zip = File(dir(context), "documents_${slug(ao.reference)}.zip")
            ZipOutputStream(zip.outputStream().buffered()).use { z ->
                pdfs.forEach { pdf ->
                    z.putNextEntry(ZipEntry(pdf.name))
                    pdf.inputStream().use { it.copyTo(z) }
                    z.closeEntry()
                }
            }
            enregistrer(context, zip, "application/zip")
            Toast.makeText(context, "Documents téléchargés : ${zip.name}", Toast.LENGTH_LONG).show()
            ouvrir(context, zip, "application/zip")
        } catch (e: Exception) {
            Toast.makeText(context, "Échec de la génération des documents", Toast.LENGTH_SHORT).show()
        }
    }

    // --- Génération d'un document -----------------------------------------

    private fun genererPdf(
        context: Context, type: TypeDocument, ao: AppelOffre, s: MaSociete, p: ParamsDocument
    ): File {
        val w = PdfWriter()
        val lieuDate = "Fait à ${p.lieu.ifBlank { s.ville.ifBlank { "……………" } }}, le ${Format.date(System.currentTimeMillis())}"
        val signataire = "${s.representantNom.ifBlank { "………………" }} — ${s.representantQualite.ifBlank { "représentant légal" }}"
        val montant = montantTexte(p)

        when (type) {
            TypeDocument.ACTE_ENGAGEMENT -> acteEngagement(w, ao, s, montant, lieuDate)
            TypeDocument.DECLARATION_HONNEUR -> declarationHonneur(w, ao, s, lieuDate)
            TypeDocument.LETTRE_MAINTIEN -> lettreMaintien(w, ao, s, p, montant, lieuDate, signataire)
        }
        w.piedDePage("Document généré par BMarche Pro — à vérifier et signer avant dépôt.")
        return w.ecrireDans(File(dir(context), "${type.slug}_${slug(ao.reference)}.pdf"))
    }

    private fun montantTexte(p: ParamsDocument): String {
        val chiffres = p.montantOffre.ifBlank { "……………" }
        val base = "$chiffres DH (TTC)"
        return if (p.montantEnLettres.isNotBlank()) "$base — soit ${p.montantEnLettres}" else base
    }

    private fun identiteConcurrent(s: MaSociete): String = buildString {
        append("Je soussigné(e) ${s.representantNom.ifBlank { "………………" }}, ")
        append("agissant en qualité de ${s.representantQualite.ifBlank { "représentant légal" }} ")
        append("au nom et pour le compte de la société ${s.raisonSociale.ifBlank { "………………" }} ")
        if (s.formeJuridique.isNotBlank()) append("(${s.formeJuridique}) ")
        if (s.capital.isNotBlank()) append("au capital de ${s.capital} DH, ")
        append("dont le siège social est à ${s.adresse.ifBlank { "………" }}${if (s.ville.isNotBlank()) ", ${s.ville}" else ""} ; ")
        append("inscrite au registre de commerce de ${s.rcVille.ifBlank { "………" }} sous le n° ${s.rcNumero.ifBlank { "………" }} ; ")
        append("ICE : ${s.ice.ifBlank { "………" }} ; identifiant fiscal : ${s.identifiantFiscal.ifBlank { "………" }} ; ")
        append("taxe professionnelle (patente) : ${s.patente.ifBlank { "………" }} ; ")
        append("affiliée à la CNSS sous le n° ${s.cnss.ifBlank { "………" }}.")
    }

    private fun acteEngagement(w: PdfWriter, ao: AppelOffre, s: MaSociete, montant: String, lieuDate: String) {
        w.titre("ACTE D'ENGAGEMENT")
        w.heading("Marché")
        w.ligneInfo("Appel d'offres n°", ao.reference)
        w.ligneInfo("Objet", ao.objet)
        w.ligneInfo("Maître d'ouvrage", ao.acheteur)
        w.espace(6f)
        w.heading("Identification du concurrent")
        w.paragraphe(identiteConcurrent(s))
        w.espace(6f)
        w.heading("Engagement")
        w.paragraphe(
            "Après avoir pris connaissance du dossier d'appel d'offres et des pièces " +
                "constituant le marché cité en objet, et après avoir apprécié à mon point de vue " +
                "et sous ma responsabilité la nature et les difficultés des prestations à exécuter :"
        )
        w.paragraphe(
            "• Je m'engage à exécuter les prestations objet du marché, conformément au cahier " +
                "des prescriptions spéciales (CPS) et moyennant les prix que j'ai établis moi-même, " +
                "pour un montant de : $montant."
        )
        w.paragraphe(
            "• Le maître d'ouvrage se libérera des sommes dues au titre du présent marché en faisant " +
                "donner crédit au compte bancaire n° ${s.rib.ifBlank { "…………………" }} ouvert auprès de " +
                "${s.banque.ifBlank { "…………………" }}."
        )
        w.signature(lieuDate, "(Signature et cachet du concurrent)")
    }

    private fun declarationHonneur(w: PdfWriter, ao: AppelOffre, s: MaSociete, lieuDate: String) {
        w.titre("DÉCLARATION SUR L'HONNEUR")
        w.ligneInfo("Appel d'offres n°", ao.reference)
        w.ligneInfo("Objet", ao.objet)
        w.ligneInfo("Maître d'ouvrage", ao.acheteur)
        w.espace(6f)
        w.paragraphe(identiteConcurrent(s))
        w.espace(6f)
        w.heading("Déclare sur l'honneur")
        w.paragraphe("1. M'engager à couvrir, dans les limites fixées dans le cahier des charges, par une police d'assurance, les risques découlant de mon activité professionnelle.")
        w.paragraphe("2. Que je ne suis pas en liquidation judiciaire ou en redressement judiciaire, et que je n'ai pas fait l'objet d'une exclusion des marchés publics.")
        w.paragraphe("3. Que je remplis les conditions prévues par la réglementation en vigueur relative aux marchés publics et que je suis en situation fiscale et sociale régulière.")
        w.paragraphe("4. M'engager à ne pas recourir par moi-même ou par personne interposée à des actes de corruption, à des manœuvres frauduleuses, et à des pratiques collusoires, à quelque titre que ce soit.")
        w.paragraphe("5. Certifier l'exactitude des renseignements contenus dans la présente déclaration sur l'honneur et dans les pièces fournies dans mon dossier de candidature.")
        w.signature(lieuDate, "(Signature et cachet du concurrent)")
    }

    private fun lettreMaintien(
        w: PdfWriter, ao: AppelOffre, s: MaSociete, p: ParamsDocument, montant: String, lieuDate: String, signataire: String
    ) {
        w.paragraphe(s.raisonSociale.ifBlank { "………………" })
        if (s.adresse.isNotBlank() || s.ville.isNotBlank()) w.paragraphe("${s.adresse}${if (s.ville.isNotBlank()) ", ${s.ville}" else ""}")
        w.espace(10f)
        w.paragraphe("À l'attention de : ${ao.acheteur}")
        w.espace(8f)
        w.heading("Objet : Confirmation du maintien de notre offre")
        w.paragraphe("Réf. : Appel d'offres n° ${ao.reference} — « ${ao.objet} »")
        w.espace(8f)
        w.paragraphe("Madame, Monsieur,")
        w.paragraphe(
            "Faisant suite à l'appel d'offres cité en objet, nous, société ${s.raisonSociale.ifBlank { "………………" }}, " +
                "représentée par ${s.representantNom.ifBlank { "………" }} en qualité de ${s.representantQualite.ifBlank { "représentant légal" }}, " +
                "avons l'honneur de vous confirmer le maintien de notre offre relative au marché susmentionné, " +
                "pour un montant de $montant."
        )
        w.paragraphe(
            "Nous vous précisons que notre offre demeure ferme et valable pour une durée de " +
                "${p.delaiValiditeJours} jours à compter de la date limite de remise des plis."
        )
        w.paragraphe("Nous restons à votre disposition pour tout complément d'information et vous prions d'agréer, Madame, Monsieur, l'expression de nos salutations distinguées.")
        w.signature(lieuDate, signataire)
    }

    // --- Enregistrement & ouverture ---------------------------------------

    private fun dir(context: Context): File {
        val racine = context.getExternalFilesDir(null) ?: context.filesDir
        return File(racine, "dossiers").apply { mkdirs() }
    }

    private fun enregistrer(context: Context, fichier: File, mime: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return
        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fichier.name)
            put(MediaStore.Downloads.MIME_TYPE, mime)
            put(MediaStore.Downloads.IS_PENDING, 1)
        }
        val resolver = context.contentResolver
        val uri: Uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values) ?: return
        resolver.openOutputStream(uri)?.use { out -> fichier.inputStream().use { it.copyTo(out) } }
        values.clear(); values.put(MediaStore.Downloads.IS_PENDING, 0)
        resolver.update(uri, values, null, null)
    }

    private fun ouvrir(context: Context, fichier: File, mime: String) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", fichier)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, mime)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try { context.startActivity(intent) } catch (e: Exception) { /* fichier dans Téléchargements */ }
    }

    private fun slug(s: String): String = s.replace(Regex("[^A-Za-z0-9]+"), "_").trim('_').take(40)
}
