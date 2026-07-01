package com.bmarche.pro.data.model

/** Catégories de publications suivies par l'application. */
enum class TypePublication(val labelFr: String, val labelAr: String) {
    MARCHE_PUBLIC("Marchés publics", "الصفقات العمومية"),
    BON_COMMANDE("Bons de commande", "سندات الطلب"),
    MARCHE_PRIVE("Marchés privés", "الصفقات الخاصة"),
    PROGRAMME_PREVISIONNEL("Programme prévisionnel", "البرنامج التوقعي"),
    RESULTAT_DEFINITIF("Résultats définitifs", "النتائج النهائية"),
    EXTRAIT_PV("Extraits de PV", "مقتطفات المحاضر"),
    DECISION_RESILIATION("Décisions de résiliation", "قرارات الفسخ");

    companion object {
        fun fromName(name: String?): TypePublication? = entries.firstOrNull { it.name == name }
    }
}
