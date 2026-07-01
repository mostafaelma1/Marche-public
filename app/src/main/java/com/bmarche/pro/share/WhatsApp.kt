package com.bmarche.pro.share

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.net.toUri
import com.bmarche.pro.data.model.AppelOffre
import com.bmarche.pro.ui.Format

/**
 * Alertes / partage WhatsApp — gratuit, via les liens `wa.me` et le partage système.
 *
 * L'envoi automatique et serveur (WhatsApp Business API) est payant ; côté client on
 * ouvre WhatsApp avec un message pré-rempli que l'utilisateur envoie en un tap
 * (à lui-même, à un groupe, à un client…).
 */
object WhatsApp {

    fun texteMarche(ao: AppelOffre): String = buildString {
        appendLine("📢 *${ao.objet}*")
        appendLine("🏛️ ${ao.acheteur}")
        appendLine("📍 ${ao.ville}")
        appendLine("💰 Estimation : ${Format.dh(ao.estimationDh)}")
        appendLine("🗓️ Date limite : ${Format.date(ao.dateLimiteEpoch)} (J-${Format.joursRestants(ao.dateLimiteEpoch)})")
        appendLine("📄 Réf : ${ao.reference}")
        append("— via BMarche Pro")
    }

    fun texteRecap(marches: List<AppelOffre>): String = buildString {
        appendLine("📢 *Alertes BMarche Pro — ${marches.size} marché(s) pour vous*")
        appendLine()
        marches.take(8).forEach { ao ->
            appendLine("• ${ao.objet} — ${ao.ville} (${Format.dh(ao.estimationDh)}), J-${Format.joursRestants(ao.dateLimiteEpoch)}")
        }
    }

    /** Partage un texte via WhatsApp (ou, à défaut, via le sélecteur de partage). */
    fun partager(context: Context, texte: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            setPackage("com.whatsapp")
            putExtra(Intent.EXTRA_TEXT, texte)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // WhatsApp non installé : on retombe sur le partage générique.
            val fallback = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, texte)
            }
            context.startActivity(Intent.createChooser(fallback, "Partager"))
        }
    }

    /**
     * Ouvre une conversation WhatsApp avec un numéro donné et un message pré-rempli.
     * Le numéro doit être au format international sans « + » ni espaces (ex : 2126…).
     */
    fun envoyerVers(context: Context, numero: String, texte: String) {
        val num = numero.filter { it.isDigit() }
        if (num.isEmpty()) {
            Toast.makeText(context, "Numéro WhatsApp invalide", Toast.LENGTH_SHORT).show()
            return
        }
        val url = "https://wa.me/$num?text=${Uri.encode(texte)}"
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Impossible d'ouvrir WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }
}
