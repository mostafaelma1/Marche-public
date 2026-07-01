package com.bmarche.pro.data.model

/**
 * Secteurs d'activité couverts par l'application. Chaque appel d'offres et chaque
 * profil utilisateur est rattaché à un ou plusieurs domaines, ce qui permet le
 * filtrage et les alertes ciblées.
 */
enum class Domaine(val labelFr: String, val labelAr: String) {
    RESTAURATION("Restauration collective", "المطاعم الجماعية"),
    NETTOYAGE("Nettoyage", "النظافة"),
    TRAVAUX("Travaux / BTP", "الأشغال والبناء"),
    FOURNITURES("Fournitures de bureau", "لوازم المكتب"),
    TRANSPORT("Transport", "النقل"),
    GARDIENNAGE("Sécurité / Gardiennage", "الأمن والحراسة"),
    INFORMATIQUE("Informatique / Digital", "المعلوميات والرقمنة"),
    ESPACES_VERTS("Espaces verts", "المساحات الخضراء");

    companion object {
        fun fromName(name: String?): Domaine? = entries.firstOrNull { it.name == name }
    }
}
