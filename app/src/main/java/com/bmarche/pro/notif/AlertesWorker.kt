package com.bmarche.pro.notif

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.bmarche.pro.data.repository.BMarcheRepository
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

/**
 * Vérifie périodiquement s'il existe des marchés correspondant au profil de
 * l'utilisateur et déclenche une notification locale. Gratuit, 100 % côté appareil.
 *
 * Quand une source de marchés distante sera branchée, ce worker ne notifiera que les
 * nouveaux marchés (via un suivi des identifiants déjà vus).
 */
class AlertesWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val repo = BMarcheRepository(applicationContext)
        val profil = repo.profilStore.profil.first()
        if (!profil.estConfigure) return Result.success()

        val marches = repo.marchesRecommandes(profil)
        NotificationHelper.notifierMarches(applicationContext, marches)
        return Result.success()
    }

    companion object {
        private const val NOM = "alertes_marches_periodiques"

        /** Planifie une vérification périodique (toutes les ~6 h). */
        fun planifier(context: Context) {
            val requete = PeriodicWorkRequestBuilder<AlertesWorker>(6, TimeUnit.HOURS)
                .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                NOM,
                ExistingPeriodicWorkPolicy.KEEP,
                requete
            )
        }
    }
}
