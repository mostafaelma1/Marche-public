package com.bmarche.pro.domain

import com.bmarche.pro.data.model.Domaine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class PrixReferenceCalculatorTest {

    @Test
    fun `rabais est calcule correctement`() {
        val a = PrixReferenceCalculator.analyser(
            estimation = 1_000_000.0,
            prixPropose = 900_000.0,
            domaine = Domaine.RESTAURATION
        )
        assertEquals(10.0, a.rabaisPct, 0.001)
    }

    @Test
    fun `prix au-dessus de l'estimation est trop haut`() {
        val a = PrixReferenceCalculator.analyser(1_000_000.0, 1_050_000.0, Domaine.RESTAURATION)
        assertEquals(PositionPrix.TROP_HAUT, a.position)
        assertTrue(a.rabaisPct < 0)
    }

    @Test
    fun `prix dans la fourchette du secteur est competitif`() {
        // Restauration : fourchette 8-15 %. 12 % est au centre.
        val a = PrixReferenceCalculator.analyser(1_000_000.0, 880_000.0, Domaine.RESTAURATION)
        assertEquals(PositionPrix.COMPETITIF, a.position)
    }

    @Test
    fun `rabais tres eleve est dangereux`() {
        val a = PrixReferenceCalculator.analyser(1_000_000.0, 700_000.0, Domaine.RESTAURATION)
        assertEquals(PositionPrix.DANGEREUX, a.position)
    }

    @Test
    fun `borne basse est inferieure a la borne haute`() {
        val a = PrixReferenceCalculator.analyser(1_000_000.0, 880_000.0, Domaine.NETTOYAGE)
        assertTrue(a.borneBasse < a.borneHaute)
        assertTrue(a.prixReference in a.borneBasse..a.borneHaute)
    }

    @Test
    fun `chance de mieux-disant croit avec le rabais`() {
        val faible = PrixReferenceCalculator.analyser(1_000_000.0, 970_000.0, Domaine.RESTAURATION)
        val fort = PrixReferenceCalculator.analyser(1_000_000.0, 870_000.0, Domaine.RESTAURATION)
        assertTrue(fort.chanceMieuxDisant > faible.chanceMieuxDisant)
    }

    @Test
    fun `estimation nulle leve une exception`() {
        assertThrows(IllegalArgumentException::class.java) {
            PrixReferenceCalculator.analyser(0.0, 100.0, Domaine.TRAVAUX)
        }
    }
}
