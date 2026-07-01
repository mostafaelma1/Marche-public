package com.bmarche.pro.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Informations d'identification de l'entreprise de l'utilisateur, servant à
 * pré-remplir les documents (acte d'engagement, déclaration sur l'honneur, lettres…).
 */
data class MaSociete(
    val raisonSociale: String = "",
    val formeJuridique: String = "",
    val capital: String = "",
    val adresse: String = "",
    val ville: String = "",
    val rcNumero: String = "",
    val rcVille: String = "",
    val ice: String = "",
    val identifiantFiscal: String = "",
    val patente: String = "",
    val cnss: String = "",
    val representantNom: String = "",
    val representantQualite: String = "",
    val telephone: String = "",
    val email: String = "",
    val banque: String = "",
    val rib: String = ""
) {
    val estRempli: Boolean
        get() = raisonSociale.isNotBlank() && representantNom.isNotBlank()
}

private val Context.societeDataStore by preferencesDataStore(name = "ma_societe")

class SocieteStore(private val context: Context) {

    private object K {
        val RAISON = stringPreferencesKey("raison_sociale")
        val FORME = stringPreferencesKey("forme_juridique")
        val CAPITAL = stringPreferencesKey("capital")
        val ADRESSE = stringPreferencesKey("adresse")
        val VILLE = stringPreferencesKey("ville")
        val RC_NUM = stringPreferencesKey("rc_numero")
        val RC_VILLE = stringPreferencesKey("rc_ville")
        val ICE = stringPreferencesKey("ice")
        val IF = stringPreferencesKey("identifiant_fiscal")
        val PATENTE = stringPreferencesKey("patente")
        val CNSS = stringPreferencesKey("cnss")
        val REP_NOM = stringPreferencesKey("representant_nom")
        val REP_QUALITE = stringPreferencesKey("representant_qualite")
        val TEL = stringPreferencesKey("telephone")
        val EMAIL = stringPreferencesKey("email")
        val BANQUE = stringPreferencesKey("banque")
        val RIB = stringPreferencesKey("rib")
    }

    val societe: Flow<MaSociete> = context.societeDataStore.data.map { p ->
        MaSociete(
            raisonSociale = p[K.RAISON].orEmpty(),
            formeJuridique = p[K.FORME].orEmpty(),
            capital = p[K.CAPITAL].orEmpty(),
            adresse = p[K.ADRESSE].orEmpty(),
            ville = p[K.VILLE].orEmpty(),
            rcNumero = p[K.RC_NUM].orEmpty(),
            rcVille = p[K.RC_VILLE].orEmpty(),
            ice = p[K.ICE].orEmpty(),
            identifiantFiscal = p[K.IF].orEmpty(),
            patente = p[K.PATENTE].orEmpty(),
            cnss = p[K.CNSS].orEmpty(),
            representantNom = p[K.REP_NOM].orEmpty(),
            representantQualite = p[K.REP_QUALITE].orEmpty(),
            telephone = p[K.TEL].orEmpty(),
            email = p[K.EMAIL].orEmpty(),
            banque = p[K.BANQUE].orEmpty(),
            rib = p[K.RIB].orEmpty()
        )
    }

    suspend fun enregistrer(s: MaSociete) {
        context.societeDataStore.edit { p ->
            p[K.RAISON] = s.raisonSociale
            p[K.FORME] = s.formeJuridique
            p[K.CAPITAL] = s.capital
            p[K.ADRESSE] = s.adresse
            p[K.VILLE] = s.ville
            p[K.RC_NUM] = s.rcNumero
            p[K.RC_VILLE] = s.rcVille
            p[K.ICE] = s.ice
            p[K.IF] = s.identifiantFiscal
            p[K.PATENTE] = s.patente
            p[K.CNSS] = s.cnss
            p[K.REP_NOM] = s.representantNom
            p[K.REP_QUALITE] = s.representantQualite
            p[K.TEL] = s.telephone
            p[K.EMAIL] = s.email
            p[K.BANQUE] = s.banque
            p[K.RIB] = s.rib
        }
    }
}
