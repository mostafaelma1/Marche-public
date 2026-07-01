package com.bmarche.pro.data.model

/** État d'avancement d'une pièce du dossier administratif. */
enum class EtatPiece(val labelFr: String, val labelAr: String) {
    A_PREPARER("À préparer", "خاص يتوجد"),
    EN_COURS("En cours", "في طور الإنجاز"),
    PRET("Prêt", "جاهز")
}

/**
 * Modèle d'une pièce type du dossier administratif d'un marché public marocain.
 * La liste par défaut est fournie par [DossierType].
 */
data class ChecklistItem(
    val cle: String,
    val libelleFr: String,
    val libelleAr: String,
    val obligatoire: Boolean = true
)

/** Pièces standard exigées dans la plupart des dossiers de marchés publics. */
object DossierType {
    val piecesStandard: List<ChecklistItem> = listOf(
        ChecklistItem("rc", "Registre de commerce (RC)", "السجل التجاري"),
        ChecklistItem("cnss", "Attestation CNSS", "شهادة الصندوق الوطني للضمان الاجتماعي"),
        ChecklistItem("fiscale", "Attestation fiscale", "الشهادة الجبائية"),
        ChecklistItem("caution", "Caution provisoire", "الضمانة المؤقتة"),
        ChecklistItem("references", "Références similaires", "مراجع مماثلة"),
        ChecklistItem("moyens_humains", "Moyens humains", "الموارد البشرية"),
        ChecklistItem("moyens_techniques", "Moyens techniques", "الموارد التقنية"),
        ChecklistItem("methodologie", "Méthodologie d'exécution", "منهجية الإنجاز"),
        ChecklistItem("planning", "Planning d'exécution", "برنامج الإنجاز"),
        ChecklistItem("declaration_honneur", "Déclaration sur l'honneur", "التصريح بالشرف")
    )
}
