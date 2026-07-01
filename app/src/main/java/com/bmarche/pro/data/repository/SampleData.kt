package com.bmarche.pro.data.repository

import com.bmarche.pro.data.model.AppelOffre
import com.bmarche.pro.data.model.Domaine
import com.bmarche.pro.data.model.Societe

/**
 * Jeu de données de démonstration. Il remplace, pour l'instant, la source réelle
 * (portail des marchés publics + résultats). L'architecture est prévue pour brancher
 * ensuite une API distante sans toucher à l'UI.
 */
object SampleData {

    private const val JOUR = 24L * 60 * 60 * 1000
    private val maintenant = System.currentTimeMillis()

    val societes: List<Societe> = listOf(
        Societe(
            id = "s1",
            nom = "Société Al Wafaa Services",
            marchesGagnes = 34,
            domaines = listOf(Domaine.NETTOYAGE, Domaine.GARDIENNAGE),
            villes = listOf("Casablanca", "Mohammedia"),
            tauxRabaisMoyen = 14.5,
            acheteursFrequents = listOf("Commune de Casablanca", "CHU Ibn Rochd")
        ),
        Societe(
            id = "s2",
            nom = "Restauration Atlas SARL",
            marchesGagnes = 21,
            domaines = listOf(Domaine.RESTAURATION),
            villes = listOf("Rabat", "Salé", "Kénitra"),
            tauxRabaisMoyen = 11.2,
            acheteursFrequents = listOf("AREF Rabat-Salé-Kénitra", "Université Mohammed V")
        ),
        Societe(
            id = "s3",
            nom = "Green Bâtiment Travaux",
            marchesGagnes = 18,
            domaines = listOf(Domaine.TRAVAUX, Domaine.ESPACES_VERTS),
            villes = listOf("Marrakech", "Benguerir"),
            tauxRabaisMoyen = 8.7,
            acheteursFrequents = listOf("Commune de Marrakech", "OCP")
        ),
        Societe(
            id = "s4",
            nom = "Bureau Pro Fournitures",
            marchesGagnes = 27,
            domaines = listOf(Domaine.FOURNITURES, Domaine.INFORMATIQUE),
            villes = listOf("Casablanca", "Rabat", "Fès"),
            tauxRabaisMoyen = 19.4,
            acheteursFrequents = listOf("Ministère de l'Éducation", "ANCFCC")
        )
    )

    val appelsOffres: List<AppelOffre> = listOf(
        AppelOffre(
            id = "ao1",
            reference = "AOO 12/2026",
            objet = "Restauration scolaire — année 2026/2027",
            acheteur = "AREF Rabat-Salé-Kénitra",
            ville = "Rabat",
            domaine = Domaine.RESTAURATION,
            estimationDh = 2_400_000.0,
            cautionProvisoireDh = 40_000.0,
            dateLimiteEpoch = maintenant + 12 * JOUR,
            descriptif = "Préparation et livraison de repas pour internats scolaires. Marché reconductible.",
            concurrentsProbables = listOf("s2"),
            piecesDossier = listOf(
                "Règlement de consultation",
                "Cahier des prescriptions spéciales (CPS)",
                "Bordereau des prix — détail estimatif",
                "Modèle d'acte d'engagement"
            )
        ),
        AppelOffre(
            id = "ao2",
            reference = "AOO 07/2026",
            objet = "Nettoyage des locaux administratifs",
            acheteur = "Commune de Casablanca",
            ville = "Casablanca",
            domaine = Domaine.NETTOYAGE,
            estimationDh = 960_000.0,
            cautionProvisoireDh = 15_000.0,
            dateLimiteEpoch = maintenant + 6 * JOUR,
            descriptif = "Nettoyage quotidien de 8 sites administratifs, fourniture des produits incluse.",
            concurrentsProbables = listOf("s1")
        ),
        AppelOffre(
            id = "ao3",
            reference = "AOO 03/2026",
            objet = "Aménagement d'espaces verts — parc urbain",
            acheteur = "Commune de Marrakech",
            ville = "Marrakech",
            domaine = Domaine.ESPACES_VERTS,
            estimationDh = 1_350_000.0,
            cautionProvisoireDh = 22_000.0,
            dateLimiteEpoch = maintenant + 20 * JOUR,
            descriptif = "Plantation, arrosage automatique et entretien sur 12 mois.",
            concurrentsProbables = listOf("s3")
        ),
        AppelOffre(
            id = "ao4",
            reference = "AOO 21/2026",
            objet = "Fourniture de matériel informatique",
            acheteur = "Ministère de l'Éducation",
            ville = "Rabat",
            domaine = Domaine.INFORMATIQUE,
            estimationDh = 780_000.0,
            cautionProvisoireDh = 12_000.0,
            dateLimiteEpoch = maintenant + 3 * JOUR,
            descriptif = "Postes de travail, imprimantes et onduleurs pour établissements scolaires.",
            concurrentsProbables = listOf("s4")
        ),
        AppelOffre(
            id = "ao5",
            reference = "AOO 15/2026",
            objet = "Gardiennage et sécurité des sites",
            acheteur = "CHU Ibn Rochd",
            ville = "Casablanca",
            domaine = Domaine.GARDIENNAGE,
            estimationDh = 1_800_000.0,
            cautionProvisoireDh = 30_000.0,
            dateLimiteEpoch = maintenant + 9 * JOUR,
            descriptif = "Gardiennage 24h/24, 30 agents, encadrement inclus.",
            concurrentsProbables = listOf("s1")
        ),
        AppelOffre(
            id = "ao6",
            reference = "AOO 09/2026",
            objet = "Travaux de réfection de voirie",
            acheteur = "Commune de Mohammedia",
            ville = "Mohammedia",
            domaine = Domaine.TRAVAUX,
            estimationDh = 3_200_000.0,
            cautionProvisoireDh = 55_000.0,
            dateLimiteEpoch = maintenant + 15 * JOUR,
            descriptif = "Réfection de 4 km de voirie, marquage et signalisation.",
            concurrentsProbables = listOf("s3"),
            piecesDossier = listOf(
                "Règlement de consultation",
                "Cahier des prescriptions spéciales (CPS)",
                "Bordereau des prix",
                "Plans et métrés"
            )
        ),
        AppelOffre(
            id = "ao7",
            reference = "AOO 18/2026",
            objet = "Fournitures de bureau — lot annuel",
            acheteur = "ANCFCC",
            ville = "Rabat",
            domaine = Domaine.FOURNITURES,
            estimationDh = 420_000.0,
            cautionProvisoireDh = 7_000.0,
            dateLimiteEpoch = maintenant + 5 * JOUR,
            descriptif = "Papeterie, consommables et petit matériel de bureau.",
            concurrentsProbables = listOf("s4")
        ),
        AppelOffre(
            id = "ao8",
            reference = "AOO 05/2026",
            objet = "Transport du personnel",
            acheteur = "OCP",
            ville = "Benguerir",
            domaine = Domaine.TRANSPORT,
            estimationDh = 1_100_000.0,
            cautionProvisoireDh = 18_000.0,
            dateLimiteEpoch = maintenant + 8 * JOUR,
            descriptif = "Transport quotidien du personnel sur plusieurs circuits.",
            concurrentsProbables = emptyList()
        )
    )

    fun societeById(id: String): Societe? = societes.firstOrNull { it.id == id }
    fun appelOffreById(id: String): AppelOffre? = appelsOffres.firstOrNull { it.id == id }
}
