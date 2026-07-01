package com.bmarche.pro.notif

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.bmarche.pro.MainActivity
import com.bmarche.pro.R
import com.bmarche.pro.data.model.AppelOffre
import com.bmarche.pro.ui.Format

/** Gestion des notifications locales d'alertes de marchés (gratuit, sans backend). */
object NotificationHelper {

    // v2 : nouvelle importance (HIGH) — un canal existant ne peut pas être ré-haussé.
    private const val CHANNEL_ID = "alertes_marches_v2"
    private const val NOTIF_ID = 2001

    fun creerCanal(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val canal = NotificationChannel(
            CHANNEL_ID,
            "Alertes marchés",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Nouvelles opportunités correspondant à votre profil."
            enableVibration(true)
        }
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(canal)
    }

    fun peutNotifier(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    /** Notifie l'utilisateur des marchés correspondant à son profil (silencieux si aucun). */
    fun notifierMarches(context: Context, marches: List<AppelOffre>) {
        if (marches.isEmpty()) return
        val titre = if (marches.size == 1) "1 marché correspond à votre profil"
        else "${marches.size} marchés correspondent à votre profil"
        val corps = marches.take(4).joinToString("\n") { ao ->
            "• ${ao.objet} — ${ao.ville} (${Format.dh(ao.estimationDh)})"
        }
        publier(context, titre, marches.first().objet, corps)
    }

    /**
     * Notification de test : s'affiche toujours (même sans marché correspondant) afin
     * de vérifier que les alertes sont bien reçues.
     */
    fun notifierTest(context: Context, marches: List<AppelOffre>) {
        if (marches.isNotEmpty()) {
            notifierMarches(context, marches)
        } else {
            publier(
                context,
                "Alertes activées ✓",
                "Les notifications fonctionnent.",
                "Vous serez prévenu dès qu'un marché correspondra à votre profil. " +
                    "Renseignez vos critères (secteurs, villes, budget) pour recevoir des alertes ciblées."
            )
        }
    }

    @SuppressLint("MissingPermission") // garanti par peutNotifier()
    private fun publier(context: Context, titre: String, texte: String, corps: String) {
        if (!peutNotifier(context)) return
        creerCanal(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pending = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_bmarche)
            .setContentTitle(titre)
            .setContentText(texte)
            .setStyle(NotificationCompat.BigTextStyle().bigText(corps))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pending)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIF_ID, notif)
    }
}
