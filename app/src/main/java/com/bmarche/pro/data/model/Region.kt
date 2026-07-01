package com.bmarche.pro.data.model

/** Les 12 régions administratives du Maroc. */
enum class Region(val labelFr: String, val labelAr: String, val villes: List<String>) {
    TANGER_TETOUAN("Tanger-Tétouan-Al Hoceïma", "طنجة تطوان الحسيمة", listOf("Tanger", "Tétouan", "Al Hoceïma", "Larache")),
    ORIENTAL("L'Oriental", "الشرق", listOf("Oujda", "Nador", "Berkane")),
    FES_MEKNES("Fès-Meknès", "فاس مكناس", listOf("Fès", "Meknès", "Taza", "Ifrane")),
    RABAT_KENITRA("Rabat-Salé-Kénitra", "الرباط سلا القنيطرة", listOf("Rabat", "Salé", "Kénitra", "Témara")),
    BENI_MELLAL("Béni Mellal-Khénifra", "بني ملال خنيفرة", listOf("Béni Mellal", "Khénifra", "Khouribga")),
    CASA_SETTAT("Casablanca-Settat", "الدار البيضاء سطات", listOf("Casablanca", "Mohammedia", "Settat", "El Jadida", "Berrechid")),
    MARRAKECH_SAFI("Marrakech-Safi", "مراكش آسفي", listOf("Marrakech", "Safi", "Benguerir", "Essaouira")),
    DRAA_TAFILALET("Drâa-Tafilalet", "درعة تافيلالت", listOf("Errachidia", "Ouarzazate", "Zagora")),
    SOUSS_MASSA("Souss-Massa", "سوس ماسة", listOf("Agadir", "Taroudant", "Tiznit")),
    GUELMIM("Guelmim-Oued Noun", "كلميم واد نون", listOf("Guelmim", "Tan-Tan")),
    LAAYOUNE("Laâyoune-Sakia El Hamra", "العيون الساقية الحمراء", listOf("Laâyoune", "Boujdour")),
    DAKHLA("Dakhla-Oued Ed-Dahab", "الداخلة وادي الذهب", listOf("Dakhla"));

    companion object {
        /** Retrouve la région d'une ville (par défaut : Casablanca-Settat). */
        fun forVille(ville: String): Region =
            entries.firstOrNull { r -> r.villes.any { it.equals(ville, ignoreCase = true) } } ?: CASA_SETTAT
    }
}
