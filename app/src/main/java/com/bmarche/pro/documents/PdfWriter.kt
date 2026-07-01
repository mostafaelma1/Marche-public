package com.bmarche.pro.documents

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import java.io.File

/**
 * Écriture de texte en flux sur des pages A4, avec retour à la ligne automatique et
 * saut de page. Suffisant pour produire des documents administratifs simples.
 */
class PdfWriter {
    companion object {
        private const val LARGEUR = 595   // A4 @ 72 dpi
        private const val HAUTEUR = 842
        private const val MARGE = 48f
    }

    private val doc = PdfDocument()
    private var numPage = 0
    private var page: PdfDocument.Page? = null
    private var y = 0f
    private val largeurUtile = LARGEUR - 2 * MARGE

    private val pTitre = paint(15f, bold = true, color = "#0E6B4F")
    private val pHeading = paint(12f, bold = true)
    private val pTexte = paint(11f)
    private val pPetit = paint(9f, color = "#777777")

    private fun paint(size: Float, bold: Boolean = false, color: String = "#111111") = Paint().apply {
        textSize = size
        this.color = Color.parseColor(color)
        typeface = if (bold) Typeface.create(Typeface.DEFAULT, Typeface.BOLD) else Typeface.DEFAULT
        isAntiAlias = true
    }

    private fun nouvellePage() {
        page?.let { doc.finishPage(it) }
        numPage += 1
        val info = PdfDocument.PageInfo.Builder(LARGEUR, HAUTEUR, numPage).create()
        page = doc.startPage(info)
        y = MARGE
    }

    private fun assurerPlace(h: Float) {
        if (page == null || y + h > HAUTEUR - MARGE) nouvellePage()
    }

    fun titre(texte: String) {
        assurerPlace(28f)
        canvas().drawText(texte, MARGE, y + 14f, pTitre)
        y += 30f
    }

    fun heading(texte: String) {
        assurerPlace(22f)
        y += 6f
        canvas().drawText(texte, MARGE, y + 12f, pHeading)
        y += 20f
    }

    fun paragraphe(texte: String, interligne: Float = 16f, paint: Paint = pTexte) {
        for (brut in texte.split("\n")) {
            if (brut.isEmpty()) { y += interligne; continue }
            var ligne = StringBuilder()
            for (mot in brut.split(" ")) {
                val essai = if (ligne.isEmpty()) mot else "$ligne $mot"
                if (paint.measureText(essai) > largeurUtile) {
                    assurerPlace(interligne)
                    canvas().drawText(ligne.toString(), MARGE, y + 12f, paint)
                    y += interligne
                    ligne = StringBuilder(mot)
                } else ligne = StringBuilder(essai)
            }
            if (ligne.isNotEmpty()) {
                assurerPlace(interligne)
                canvas().drawText(ligne.toString(), MARGE, y + 12f, paint)
                y += interligne
            }
        }
    }

    fun ligneInfo(label: String, valeur: String) {
        assurerPlace(16f)
        val c = canvas()
        c.drawText("$label :", MARGE, y + 12f, pTexte)
        c.drawText(valeur.ifBlank { "……………………" }, MARGE + 170f, y + 12f, pTexte)
        y += 16f
    }

    fun espace(h: Float = 12f) { y += h }

    fun signature(lieuDate: String, mention: String) {
        assurerPlace(70f)
        espace(24f)
        canvas().drawText(lieuDate, LARGEUR - MARGE - 220f, y + 12f, pTexte)
        y += 20f
        canvas().drawText(mention, LARGEUR - MARGE - 220f, y + 12f, pTexte)
        y += 40f
    }

    fun piedDePage(texte: String) {
        val c = canvas()
        c.drawText(texte, MARGE, HAUTEUR - MARGE + 20f, pPetit)
    }

    private fun canvas() = (page ?: run { nouvellePage(); page!! }).canvas

    fun ecrireDans(fichier: File): File {
        page?.let { doc.finishPage(it); page = null }
        fichier.outputStream().use { doc.writeTo(it) }
        doc.close()
        return fichier
    }
}
