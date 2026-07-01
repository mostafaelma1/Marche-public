package com.bmarche.pro.data.repository

import com.bmarche.pro.data.model.AppelOffre
import com.bmarche.pro.data.model.Domaine
import com.bmarche.pro.data.model.Societe
import com.bmarche.pro.data.model.TypePublication

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
            ),
            email = "marches@ariase.gov.ma",
            telephone = "05-37-00-00-00",
            telecopieur = "05-37-00-00-01"
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
        ),
        AppelOffre(
            id = "ao9", reference = "AOO 22/2026",
            objet = "Réhabilitation d'un établissement scolaire",
            acheteur = "AREF Tanger-Tétouan-Al Hoceïma", ville = "Tanger",
            domaine = Domaine.TRAVAUX, estimationDh = 2_100_000.0, cautionProvisoireDh = 35_000.0,
            dateLimiteEpoch = maintenant + 11 * JOUR,
            descriptif = "Réhabilitation complète : maçonnerie, électricité, plomberie.",
            email = "marches@aref-ttah.gov.ma", telephone = "05-39-00-00-00"
        ),
        AppelOffre(
            id = "ao10", reference = "AOO 31/2026",
            objet = "Nettoyage des établissements de santé",
            acheteur = "Délégation de la Santé — Oujda", ville = "Oujda",
            domaine = Domaine.NETTOYAGE, estimationDh = 720_000.0, cautionProvisoireDh = 12_000.0,
            dateLimiteEpoch = maintenant + 7 * JOUR,
            descriptif = "Nettoyage et désinfection de 5 centres de santé."
        ),
        AppelOffre(
            id = "ao11", reference = "AOO 12/2026",
            objet = "Fourniture de mobilier scolaire",
            acheteur = "Commune de Fès", ville = "Fès",
            domaine = Domaine.FOURNITURES, estimationDh = 540_000.0, cautionProvisoireDh = 9_000.0,
            dateLimiteEpoch = maintenant + 4 * JOUR,
            descriptif = "Tables, chaises et tableaux pour écoles primaires."
        ),
        AppelOffre(
            id = "ao12", reference = "AOO 08/2026",
            objet = "Entretien des espaces verts urbains",
            acheteur = "Commune de Meknès", ville = "Meknès",
            domaine = Domaine.ESPACES_VERTS, estimationDh = 880_000.0, cautionProvisoireDh = 14_000.0,
            dateLimiteEpoch = maintenant + 13 * JOUR,
            descriptif = "Entretien des jardins publics et arrosage automatique."
        ),
        AppelOffre(
            id = "ao13", reference = "AOO 27/2026",
            objet = "Gardiennage des sites communaux",
            acheteur = "Commune d'Agadir", ville = "Agadir",
            domaine = Domaine.GARDIENNAGE, estimationDh = 1_450_000.0, cautionProvisoireDh = 24_000.0,
            dateLimiteEpoch = maintenant + 10 * JOUR,
            descriptif = "Gardiennage de 12 sites, 24h/24."
        ),
        AppelOffre(
            id = "ao14", reference = "AOO 19/2026",
            objet = "Restauration collective — hôpital",
            acheteur = "CHR Agadir", ville = "Taroudant",
            domaine = Domaine.RESTAURATION, estimationDh = 1_650_000.0, cautionProvisoireDh = 28_000.0,
            dateLimiteEpoch = maintenant + 16 * JOUR,
            descriptif = "Préparation des repas pour patients et personnel."
        ),
        AppelOffre(
            id = "ao15", reference = "AOO 04/2026",
            objet = "Travaux de construction d'un souk",
            acheteur = "Commune de Béni Mellal", ville = "Béni Mellal",
            domaine = Domaine.TRAVAUX, estimationDh = 4_500_000.0, cautionProvisoireDh = 70_000.0,
            dateLimiteEpoch = maintenant + 22 * JOUR,
            descriptif = "Construction d'un marché couvert de 80 locaux.",
            piecesDossier = listOf("Règlement de consultation", "CPS", "Bordereau des prix", "Plans architecturaux")
        ),
        AppelOffre(
            id = "ao16", reference = "AOO 14/2026",
            objet = "Fourniture de matériel informatique",
            acheteur = "Province d'Errachidia", ville = "Errachidia",
            domaine = Domaine.INFORMATIQUE, estimationDh = 690_000.0, cautionProvisoireDh = 11_000.0,
            dateLimiteEpoch = maintenant + 6 * JOUR,
            descriptif = "Ordinateurs, serveurs et équipements réseau."
        ),
        AppelOffre(
            id = "ao17", reference = "AOO 02/2026",
            objet = "Transport scolaire en milieu rural",
            acheteur = "Commune de Dakhla", ville = "Dakhla",
            domaine = Domaine.TRANSPORT, estimationDh = 980_000.0, cautionProvisoireDh = 16_000.0,
            dateLimiteEpoch = maintenant + 9 * JOUR,
            descriptif = "Transport quotidien des élèves sur plusieurs circuits ruraux."
        ),
        AppelOffre(
            id = "ao18", reference = "AOO 06/2026",
            objet = "Aménagement de la corniche",
            acheteur = "Commune de Laâyoune", ville = "Laâyoune",
            domaine = Domaine.TRAVAUX, estimationDh = 3_800_000.0, cautionProvisoireDh = 60_000.0,
            dateLimiteEpoch = maintenant + 18 * JOUR,
            descriptif = "Aménagement paysager et éclairage de la corniche."
        ),
        AppelOffre(
            id = "ao19", reference = "AOO 25/2026",
            objet = "Fourniture de fournitures de bureau",
            acheteur = "Province de Guelmim", ville = "Guelmim",
            domaine = Domaine.FOURNITURES, estimationDh = 320_000.0, cautionProvisoireDh = 5_000.0,
            dateLimiteEpoch = maintenant + 5 * JOUR,
            descriptif = "Consommables et petit matériel de bureau."
        ),
        AppelOffre(
            id = "ao20", reference = "AOO 11/2026",
            objet = "Nettoyage de la voirie",
            acheteur = "Commune de Nador", ville = "Nador",
            domaine = Domaine.NETTOYAGE, estimationDh = 1_050_000.0, cautionProvisoireDh = 17_000.0,
            dateLimiteEpoch = maintenant + 12 * JOUR,
            descriptif = "Nettoyage et collecte des déchets sur les axes principaux."
        ),
        AppelOffre(
            id = "ao21", reference = "AOO 17/2026",
            objet = "Restauration scolaire",
            acheteur = "AREF Marrakech-Safi", ville = "Safi",
            domaine = Domaine.RESTAURATION, estimationDh = 1_900_000.0, cautionProvisoireDh = 31_000.0,
            dateLimiteEpoch = maintenant + 14 * JOUR,
            descriptif = "Repas pour internats et cantines scolaires."
        ),
        // --- Bons de commande ---
        AppelOffre(
            id = "bc1", reference = "BC 45/2026",
            objet = "Achat de consommables informatiques",
            acheteur = "Commune de Rabat", ville = "Rabat",
            domaine = Domaine.INFORMATIQUE, estimationDh = 120_000.0, cautionProvisoireDh = 0.0,
            dateLimiteEpoch = maintenant + 4 * JOUR,
            descriptif = "Cartouches, toners et supports de stockage.",
            type = TypePublication.BON_COMMANDE
        ),
        AppelOffre(
            id = "bc2", reference = "BC 51/2026",
            objet = "Fourniture de produits d'entretien",
            acheteur = "Province de Fès", ville = "Fès",
            domaine = Domaine.NETTOYAGE, estimationDh = 90_000.0, cautionProvisoireDh = 0.0,
            dateLimiteEpoch = maintenant + 6 * JOUR,
            descriptif = "Produits et matériel de nettoyage.",
            type = TypePublication.BON_COMMANDE
        ),
        // --- Marchés privés ---
        AppelOffre(
            id = "mp1", reference = "MP 03/2026",
            objet = "Travaux d'aménagement d'un centre commercial",
            acheteur = "Groupe immobilier privé", ville = "Casablanca",
            domaine = Domaine.TRAVAUX, estimationDh = 6_800_000.0, cautionProvisoireDh = 100_000.0,
            dateLimiteEpoch = maintenant + 20 * JOUR,
            descriptif = "Aménagement intérieur et façades d'un centre commercial privé.",
            type = TypePublication.MARCHE_PRIVE
        ),
        // --- Programme prévisionnel ---
        AppelOffre(
            id = "pp1", reference = "PP 2026",
            objet = "Programme prévisionnel des achats 2026",
            acheteur = "Ministère de la Santé", ville = "Rabat",
            domaine = Domaine.FOURNITURES, estimationDh = 0.0, cautionProvisoireDh = 0.0,
            dateLimiteEpoch = maintenant + 60 * JOUR,
            descriptif = "Prévisions des marchés à lancer durant l'année 2026.",
            type = TypePublication.PROGRAMME_PREVISIONNEL
        ),
        // --- Résultats définitifs ---
        AppelOffre(
            id = "rd1", reference = "RD 08/2026",
            objet = "Résultat — Nettoyage des locaux administratifs",
            acheteur = "Commune de Casablanca", ville = "Casablanca",
            domaine = Domaine.NETTOYAGE, estimationDh = 960_000.0, cautionProvisoireDh = 0.0,
            dateLimiteEpoch = maintenant,
            descriptif = "Marché attribué à la société Al Wafaa Services au montant de 845 000 DH.",
            type = TypePublication.RESULTAT_DEFINITIF
        ),
        AppelOffre(
            id = "rd2", reference = "RD 12/2026",
            objet = "Résultat — Restauration scolaire",
            acheteur = "AREF Rabat-Salé-Kénitra", ville = "Rabat",
            domaine = Domaine.RESTAURATION, estimationDh = 2_400_000.0, cautionProvisoireDh = 0.0,
            dateLimiteEpoch = maintenant,
            descriptif = "Marché attribué à Restauration Atlas SARL au montant de 2 160 000 DH.",
            type = TypePublication.RESULTAT_DEFINITIF
        ),
        // --- Extraits de PV ---
        AppelOffre(
            id = "pv1", reference = "PV 05/2026",
            objet = "Extrait de PV — Travaux de voirie",
            acheteur = "Commune de Mohammedia", ville = "Mohammedia",
            domaine = Domaine.TRAVAUX, estimationDh = 3_200_000.0, cautionProvisoireDh = 0.0,
            dateLimiteEpoch = maintenant,
            descriptif = "Procès-verbal de la séance d'ouverture des plis.",
            type = TypePublication.EXTRAIT_PV
        ),
        // --- Décisions de résiliation ---
        AppelOffre(
            id = "dr1", reference = "DR 02/2026",
            objet = "Décision de résiliation — Gardiennage",
            acheteur = "CHU Ibn Rochd", ville = "Casablanca",
            domaine = Domaine.GARDIENNAGE, estimationDh = 1_800_000.0, cautionProvisoireDh = 0.0,
            dateLimiteEpoch = maintenant,
            descriptif = "Résiliation du marché pour non-respect des clauses contractuelles.",
            type = TypePublication.DECISION_RESILIATION
        )
    )

    fun societeById(id: String): Societe? = societes.firstOrNull { it.id == id }
    fun appelOffreById(id: String): AppelOffre? = appelsOffres.firstOrNull { it.id == id }
}
