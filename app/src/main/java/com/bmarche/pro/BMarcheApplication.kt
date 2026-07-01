package com.bmarche.pro

import android.app.Application
import com.bmarche.pro.data.repository.BMarcheRepository
import com.bmarche.pro.notif.AlertesWorker
import com.bmarche.pro.notif.NotificationHelper

/**
 * Application unique. Expose une instance partagée du [BMarcheRepository] pour éviter,
 * dans cette version MVP, d'introduire une bibliothèque d'injection de dépendances.
 */
class BMarcheApplication : Application() {
    val repository: BMarcheRepository by lazy { BMarcheRepository(this) }

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.creerCanal(this)
        AlertesWorker.planifier(this)
    }
}
