package com.bmarche.pro

import android.app.Application
import com.bmarche.pro.data.repository.BMarcheRepository

/**
 * Application unique. Expose une instance partagée du [BMarcheRepository] pour éviter,
 * dans cette version MVP, d'introduire une bibliothèque d'injection de dépendances.
 */
class BMarcheApplication : Application() {
    val repository: BMarcheRepository by lazy { BMarcheRepository(this) }
}
