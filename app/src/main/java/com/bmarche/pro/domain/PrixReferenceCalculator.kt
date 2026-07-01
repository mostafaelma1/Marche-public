package com.bmarche.pro.domain

import com.bmarche.pro.data.model.Domaine
import kotlin.math.roundToLong

/**
 * Position d'un prix proposé par rapport à l'estimation administrative et aux
 * habitudes de rabais du secteur.
 */
enum class PositionPrix { TROP_HAUT, HAUT, COMPETITIF, AGRESSIF, DANGEREUX }

/**
 * Résultat de l'analyse d'un prix de soumission.
 *
 * @param rabaisPct rabais du prix proposé par rapport à l'estimation, en % (positif = moins cher).
 * @param position appréciation qualitative de ce rabais.
 * @param prixReference prix "au centre" de l'intervalle habituel du secteur.
 * @param borneBasse / borneHaute intervalle de prix considéré comme sûr et compétitif.
 * @param chanceMieuxDisant probabilité indicative (0..1) d'être le moins-disant.
 */
data class AnalysePrix(
    val rabaisPct: Double,
    val position: PositionPrix,
    val prixReference: Long,
    val borneBasse: Long,
    val borneHaute: Long,
    val chanceMieuxDisant: Double
)

/**
 * Calcule un prix de référence et positionne le prix de l'utilisateur.
 *
 * Le modèle est volontairement simple et transparent (pas de "boîte noire") : il
 * s'appuie sur des fourchettes de rabais observées par secteur. Ces fourchettes
 * pourront être affinées plus tard avec les données réelles des marchés.
 */
object PrixReferenceCalculator {

    /** Fourchette de rabais habituel (min%, max%) observée par domaine. */
    private val fourchetteRabais: Map<Domaine, Pair<Double, Double>> = mapOf(
        Domaine.RESTAURATION to (8.0 to 15.0),
        Domaine.NETTOYAGE to (10.0 to 20.0),
        Domaine.TRAVAUX to (5.0 to 12.0),
        Domaine.FOURNITURES to (12.0 to 25.0),
        Domaine.TRANSPORT to (7.0 to 15.0),
        Domaine.GARDIENNAGE to (6.0 to 12.0),
        Domaine.INFORMATIQUE to (5.0 to 18.0),
        Domaine.ESPACES_VERTS to (8.0 to 16.0)
    )

    /** Rabais au-delà duquel l'offre devient anormalement basse (risque de rejet). */
    private const val SEUIL_DANGER_PCT = 25.0

    fun fourchette(domaine: Domaine): Pair<Double, Double> =
        fourchetteRabais[domaine] ?: (8.0 to 15.0)

    fun analyser(estimation: Double, prixPropose: Double, domaine: Domaine): AnalysePrix {
        require(estimation > 0) { "L'estimation doit être supérieure à 0." }

        val (minPct, maxPct) = fourchette(domaine)
        val rabaisPct = (estimation - prixPropose) / estimation * 100.0

        val borneHaute = appliquerRabais(estimation, minPct)   // moins de rabais => prix plus haut
        val borneBasse = appliquerRabais(estimation, maxPct)   // plus de rabais => prix plus bas
        val prixReference = appliquerRabais(estimation, (minPct + maxPct) / 2.0)

        val position = when {
            rabaisPct < 0 -> PositionPrix.TROP_HAUT
            rabaisPct < minPct -> PositionPrix.HAUT
            rabaisPct <= maxPct -> PositionPrix.COMPETITIF
            rabaisPct <= SEUIL_DANGER_PCT -> PositionPrix.AGRESSIF
            else -> PositionPrix.DANGEREUX
        }

        val chance = estimerChanceMieuxDisant(rabaisPct, minPct, maxPct)

        return AnalysePrix(
            rabaisPct = rabaisPct,
            position = position,
            prixReference = prixReference,
            borneBasse = borneBasse,
            borneHaute = borneHaute,
            chanceMieuxDisant = chance
        )
    }

    private fun appliquerRabais(estimation: Double, rabaisPct: Double): Long =
        (estimation * (1 - rabaisPct / 100.0)).roundToLong()

    /**
     * Estime, entre 0 et 1, la probabilité d'être le moins-disant. La chance croît
     * avec le rabais jusqu'au haut de la fourchette, puis se stabilise (au-delà, on
     * gagne surtout en risque, pas en probabilité utile).
     */
    private fun estimerChanceMieuxDisant(rabaisPct: Double, minPct: Double, maxPct: Double): Double {
        return when {
            rabaisPct <= minPct -> (rabaisPct / minPct).coerceIn(0.0, 1.0) * 0.4
            rabaisPct <= maxPct -> {
                val t = (rabaisPct - minPct) / (maxPct - minPct)
                0.4 + t * 0.5
            }
            else -> 0.9
        }.coerceIn(0.0, 1.0)
    }
}
