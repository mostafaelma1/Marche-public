package com.bmarche.pro.dossier

import android.content.ContentValues
import android.content.Context
import android.content.Intent
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

/**
 * Génère la fiche « dossier » d'un marché au format PDF (récapitulatif + checklist
 * administrative), l'enregistre dans le dossier Téléchargements et l'ouvre.
 */
object DossierPdfGenerator {

    private const val LARGEUR = 595  // A4 @ 72 dpi
    private const val HAUTEUR = 842
    private const val MARGE = 40f

    /** Point d'entrée : génère, enregistre dans Téléchargements, puis ouvre le PDF. */
    fun telecharger(context: Context, ao: AppelOffre, etats: Map<String, EtatPiece>) {
        try {
            val fichier = genererFichier(context, ao, etats)
            enregistrerDansTelechargements(context, fichier)
            Toast.makeText(context, "Dossier téléchargé : ${fichier.name}", Toast.LENGTH_LONG).show()
            ouvrir(context, fichier)
        } catch (e: Exception) {
            Toast.makeText(context, "Échec de la génération du dossier", Toast.LENGTH_SHORT).show()
        }
    }

    private fun genererFichier(context: Context, ao: AppelOffre, etats: Map<String, EtatPiece>): File {
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

        canvas.drawText("BMarche Pro — Dossier de marché", MARGE, y, titre)
        y += 26f
        canvas.drawText(ao.reference, MARGE, y, sousTitre)
        y += 22f

        // Objet (possiblement sur plusieurs lignes).
        y = dessinerParagraphe(canvas, "Objet : ${ao.objet}", MARGE, y, texte, LARGEUR - 2 * MARGE)
        y += 8f

        val infos = listOf(
            "Acheteur" to ao.acheteur,
            "Ville" to ao.ville,
            "Secteur" to ao.domaine.labelFr,
            "Estimation" to Format.dh(ao.estimationDh),
            "Caution provisoire" to Format.dh(ao.cautionProvisoireDh),
            "Date limite" to "${Format.date(ao.dateLimiteEpoch)} (J-${Format.joursRestants(ao.dateLimiteEpoch)})"
        )
        infos.forEach { (l, v) ->
            canvas.drawText("$l :", MARGE, y, label)
            canvas.drawText(v, MARGE + 140f, y, valeur)
            y += 18f
        }

        if (ao.descriptif.isNotBlank()) {
            y += 6f
            canvas.drawText("Descriptif", MARGE, y, sousTitre)
            y += 16f
            y = dessinerParagraphe(canvas, ao.descriptif, MARGE, y, texte, LARGEUR - 2 * MARGE)
        }

        // Checklist du dossier administratif.
        y += 12f
        canvas.drawText("Dossier administratif — pièces à fournir", MARGE, y, sousTitre)
        y += 18f
        DossierType.piecesStandard.forEach { piece ->
            val etat = etats[piece.cle] ?: EtatPiece.A_PREPARER
            val coche = if (etat == EtatPiece.PRET) "[x]" else "[ ]"
            canvas.drawText("$coche  ${piece.libelleFr}", MARGE, y, texte)
            canvas.drawText(etat.labelFr, LARGEUR - MARGE - 90f, y, label)
            y += 17f
        }

        y += 14f
        val pied = Paint().apply { color = Color.parseColor("#888888"); textSize = 9f }
        canvas.drawText(
            "Généré par BMarche Pro le ${Format.date(System.currentTimeMillis())} — document indicatif.",
            MARGE, HAUTEUR - MARGE, pied
        )

        doc.finishPage(page)

        val racine = context.getExternalFilesDir(null) ?: context.filesDir
        val dir = File(racine, "dossiers").apply { mkdirs() }
        val nom = "dossier_${ao.reference.replace(Regex("[^A-Za-z0-9]"), "_")}.pdf"
        val fichier = File(dir, nom)
        fichier.outputStream().use { doc.writeTo(it) }
        doc.close()
        return fichier
    }

    /** Dessine un texte en le coupant en lignes selon la largeur disponible. */
    private fun dessinerParagraphe(
        canvas: android.graphics.Canvas,
        contenu: String,
        x: Float,
        yDepart: Float,
        paint: Paint,
        largeurMax: Float
    ): Float {
        var y = yDepart
        val mots = contenu.split(" ")
        var ligne = StringBuilder()
        for (mot in mots) {
            val essai = if (ligne.isEmpty()) mot else "$ligne $mot"
            if (paint.measureText(essai) > largeurMax) {
                canvas.drawText(ligne.toString(), x, y, paint)
                y += 16f
                ligne = StringBuilder(mot)
            } else {
                ligne = StringBuilder(essai)
            }
        }
        if (ligne.isNotEmpty()) {
            canvas.drawText(ligne.toString(), x, y, paint)
            y += 16f
        }
        return y
    }

    /** Copie le PDF dans le dossier public « Téléchargements » (API 29+). */
    private fun enregistrerDansTelechargements(context: Context, fichier: File) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return
        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fichier.name)
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
            put(MediaStore.Downloads.IS_PENDING, 1)
        }
        val resolver = context.contentResolver
        val uri: Uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values) ?: return
        resolver.openOutputStream(uri)?.use { out -> fichier.inputStream().use { it.copyTo(out) } }
        values.clear()
        values.put(MediaStore.Downloads.IS_PENDING, 0)
        resolver.update(uri, values, null, null)
    }

    private fun ouvrir(context: Context, fichier: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", fichier)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // Aucun lecteur PDF : le fichier reste disponible dans Téléchargements.
        }
    }
}
