package com.bmarche.pro.ui

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/** Utilitaires de formatage (dirhams, dates, échéances) partagés par l'UI. */
object Format {

    fun dh(montant: Double): String {
        val entier = montant.toLong()
        val groupe = "%,d".format(entier).replace(',', ' ')
        return "$groupe DH"
    }

    fun dh(montant: Long): String = dh(montant.toDouble())

    fun date(epoch: Long): String =
        SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(Date(epoch))

    /** Nombre de jours restants (>= 0) avant l'échéance. */
    fun joursRestants(epoch: Long): Long {
        val diff = epoch - System.currentTimeMillis()
        return TimeUnit.MILLISECONDS.toDays(diff).coerceAtLeast(0)
    }

    fun pct(valeur: Double): String = "%.1f %%".format(valeur)
}
