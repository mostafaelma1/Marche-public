package com.bmarche.pro.dossier

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bmarche.pro.data.model.AppelOffre
import com.bmarche.pro.data.model.DossierType
import com.bmarche.pro.data.model.EtatPiece
import com.bmarche.pro.ui.Format
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Génère le dossier téléchargeable d'un marché :
 *  - **PDF** simple lorsque le marché n'a qu'une pièce (fiche récapitulative) ;
 *  - **ZIP** lorsque le dossier de consultation comporte plusieurs pièces (la fiche
 *    récap + une note par pièce).
 *
 * Le fichier est enregistré dans « Téléchargements » puis ouvert.
 */
object DossierGenerator {

    private const val LARGEUR = 595  // A4 @ 72 dpi
    private const val HAUTEUR = 842
    private const val MARGE = 40f

    /** « ZIP » ou « PDF » selon la composition du dossier — pour l'intitulé du bouton. */
    fun formatLabel(ao: AppelOffre): String = if (ao.piecesDossier.size > 1) "ZIP" else "PDF"

    fun telecharger(context: Context, ao: AppelOffre, etats: Map<String, EtatPiece>) {
        try {
            val fiche = genererFichePdf(context, ao, etats)
            val (fichier, mime) = if (ao.piecesDossier.size > 1) {
                genererZip(context, ao, fiche) to "application/zip"
            } else {
                fiche to "application/pdf"
            }
            enregistrerDansTelechargements(context, fichier, mime)
            Toast.makeText(context, "Dossier téléchargé : ${fichier.name}", Toast.LENGTH_LONG).show()
            ouvrir(context, fichier, mime)
        } catch (e: Exception) {
            Toast.makeText(context, "Échec de la génération du dossier", Toast.LENGTH_SHORT).show()
        }
    }

    // --- PDF fiche récapitulative -----------------------------------------

    private fun genererFichePdf(context: Context, ao: AppelOffre, etats: Map<String, EtatPiece>): File {
        val doc = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(LARGEUR, HAUTEUR, 1).create()
        val page = doc.startPage(pageInfo)
        val canvas = page.canvas

        val titre = Paint().apply { color = Color.parseColor("#0E6B4F"); textSize = 20f; isFakeBoldText = true }
        val sousTitre = Paint().apply { color = Color.parseColor("#0E6B4F"); textSize = 13f; isFakeBoldText = true }
        val label = Paint().apply { color = Color.parseColor("#555555"); textSize = 11f }
        val valeur = Paint().apply { color = Color.parseColor("#111111"); textSize = 11f; isFakeBoldText = true }
        val texte = Paint().apply { color = Color.parseColor("#111111"); textSize = 11f }

        var y = MARGE + 10f
        canvas.drawText("BMarche Pro — Fiche de marché", MARGE, y, titre)
        y += 26f
        canvas.drawText(ao.reference, MARGE, y, sousTitre)
        y += 22f
        y = paragraphe(canvas, "Objet : ${ao.objet}", MARGE, y, texte, LARGEUR - 2 * MARGE)
        y += 8f

        listOf(
            "Acheteur" to ao.acheteur,
            "Ville" to ao.ville,
            "Secteur" to ao.domaine.labelFr,
            "Estimation" to Format.dh(ao.estimationDh),
            "Caution provisoire" to Format.dh(ao.cautionProvisoireDh),
            "Date limite" to "${Format.date(ao.dateLimiteEpoch)} (J-${Format.joursRestants(ao.dateLimiteEpoch)})"
        ).forEach { (l, v) ->
            canvas.drawText("$l :", MARGE, y, label)
            canvas.drawText(v, MARGE + 140f, y, valeur)
            y += 18f
        }

        if (ao.descriptif.isNotBlank()) {
            y += 6f
            canvas.drawText("Descriptif", MARGE, y, sousTitre); y += 16f
            y = paragraphe(canvas, ao.descriptif, MARGE, y, texte, LARGEUR - 2 * MARGE)
        }

        if (ao.piecesDossier.isNotEmpty()) {
            y += 10f
            canvas.drawText("Pièces du dossier de consultation", MARGE, y, sousTitre); y += 16f
            ao.piecesDossier.forEach { p -> canvas.drawText("• $p", MARGE, y, texte); y += 16f }
        }

        y += 10f
        canvas.drawText("Dossier administratif — pièces à fournir", MARGE, y, sousTitre); y += 18f
        DossierType.piecesStandard.forEach { piece ->
            val etat = etats[piece.cle] ?: EtatPiece.A_PREPARER
            val coche = if (etat == EtatPiece.PRET) "[x]" else "[ ]"
            canvas.drawText("$coche  ${piece.libelleFr}", MARGE, y, texte)
            canvas.drawText(etat.labelFr, LARGEUR - MARGE - 90f, y, label)
            y += 17f
        }

        val pied = Paint().apply { color = Color.parseColor("#888888"); textSize = 9f }
        canvas.drawText(
            "Généré par BMarche Pro le ${Format.date(System.currentTimeMillis())} — document indicatif.",
            MARGE, HAUTEUR - MARGE, pied
        )
        doc.finishPage(page)

        val fichier = File(dossierDir(context), "fiche_${slug(ao.reference)}.pdf")
        fichier.outputStream().use { doc.writeTo(it) }
        doc.close()
        return fichier
    }

    // --- ZIP (dossier multi-pièces) ---------------------------------------

    private fun genererZip(context: Context, ao: AppelOffre, fiche: File): File {
        val zipFile = File(dossierDir(context), "dossier_${slug(ao.reference)}.zip")
        ZipOutputStream(zipFile.outputStream().buffered()).use { zip ->
            // 1) la fiche récap
            zip.putNextEntry(ZipEntry("00_${fiche.name}"))
            fiche.inputStream().use { it.copyTo(zip) }
            zip.closeEntry()
            // 2) une note par pièce du dossier de consultation
            ao.piecesDossier.forEachIndexed { i, piece ->
                val nom = "%02d_%s.txt".format(i + 1, slug(piece))
                zip.putNextEntry(ZipEntry(nom))
                zip.write(contenuNotePiece(ao, piece).toByteArray(Charsets.UTF_8))
                zip.closeEntry()
            }
        }
        return zipFile
    }

    private fun contenuNotePiece(ao: AppelOffre, piece: String): String = buildString {
        appendLine("BMarche Pro — Dossier du marché ${ao.reference}")
        appendLine("Pièce : $piece")
        appendLine("Marché : ${ao.objet}")
        appendLine("Acheteur : ${ao.acheteur}")
        appendLine()
        appendLine("Cette pièce fait partie du dossier de consultation (DCE).")
        appendLine("Récupérez la version officielle auprès de l'acheteur / du portail des")
        appendLine("marchés, puis complétez-la avant le dépôt.")
    }

    // --- Enregistrement & ouverture ---------------------------------------

    private fun dossierDir(context: Context): File {
        val racine = context.getExternalFilesDir(null) ?: context.filesDir
        return File(racine, "dossiers").apply { mkdirs() }
    }

    private fun enregistrerDansTelechargements(context: Context, fichier: File, mime: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return
        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fichier.name)
            put(MediaStore.Downloads.MIME_TYPE, mime)
            put(MediaStore.Downloads.IS_PENDING, 1)
        }
        val resolver = context.contentResolver
        val uri: Uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values) ?: return
        resolver.openOutputStream(uri)?.use { out -> fichier.inputStream().use { it.copyTo(out) } }
        values.clear()
        values.put(MediaStore.Downloads.IS_PENDING, 0)
        resolver.update(uri, values, null, null)
    }

    private fun ouvrir(context: Context, fichier: File, mime: String) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", fichier)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, mime)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // Aucun lecteur adapté : le fichier reste dans Téléchargements.
        }
    }

    // --- Utilitaires ------------------------------------------------------

    private fun slug(s: String): String =
        s.replace(Regex("[^A-Za-z0-9]+"), "_").trim('_').take(40)

    private fun paragraphe(
        canvas: Canvas, contenu: String, x: Float, yDepart: Float, paint: Paint, largeurMax: Float
    ): Float {
        var y = yDepart
        var ligne = StringBuilder()
        for (mot in contenu.split(" ")) {
            val essai = if (ligne.isEmpty()) mot else "$ligne $mot"
            if (paint.measureText(essai) > largeurMax) {
                canvas.drawText(ligne.toString(), x, y, paint); y += 16f
                ligne = StringBuilder(mot)
            } else ligne = StringBuilder(essai)
        }
        if (ligne.isNotEmpty()) { canvas.drawText(ligne.toString(), x, y, paint); y += 16f }
        return y
    }
}
