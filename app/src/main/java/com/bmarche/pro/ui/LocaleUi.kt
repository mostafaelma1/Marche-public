package com.bmarche.pro.ui

import com.bmarche.pro.data.model.Domaine
import com.bmarche.pro.data.model.EtatPiece
import com.bmarche.pro.data.model.Region
import com.bmarche.pro.data.model.TypePublication
import java.util.Locale

/**
 * Libellés localisés des entités métier. L'activité est recréée au changement de
 * langue (AppCompatDelegate), la valeur est donc réévaluée à chaque affichage.
 */
fun estArabe(): Boolean = Locale.getDefault().language == "ar"

fun Domaine.label(): String = if (estArabe()) labelAr else labelFr
fun Region.label(): String = if (estArabe()) labelAr else labelFr
fun TypePublication.label(): String = if (estArabe()) labelAr else labelFr
fun EtatPiece.label(): String = if (estArabe()) labelAr else labelFr
