# BMarche Pro 📱

Application Android d'assistance aux marchés publics (appels d'offres) pour les
TPE/PME marocaines.

> **L'idée forte** : ce n'est pas « encore un site d'appels d'offres ». BMarche Pro
> aide l'entreprise **avant le dépôt** — trouver le bon marché, analyser la
> concurrence, et estimer le prix le plus proche du gagnant.

## 🎯 Fonctionnalités (MVP — version actuelle)

| # | Fonction | État |
|---|----------|------|
| 0 | **Page d'accueil par région** (12 régions du Maroc) avec compteurs + total | ✅ |
| 1 | Liste des appels d'offres | ✅ |
| 2 | Filtres par secteur & ville + recherche | ✅ |
| 3 | Fiche détaillée d'un marché | ✅ |
| 4 | Calcul du prix de référence & positionnement | ✅ |
| 5 | Checklist du dossier administratif (suivi prêt / en cours / à préparer) | ✅ |
| 6 | Favoris (persistés) | ✅ |
| 7 | Analyse de la concurrence (historique sociétés) | ✅ |
| 8 | Profil d'alertes (secteurs, villes, budget, mots-clés) | ✅ |
| 9 | **Notifications locales** d'alertes (gratuit, sans Google Play Services) | ✅ |
| 10 | **Alertes / partage WhatsApp** (récap + partage par marché, gratuit) | ✅ |
| 11 | **Téléchargement du dossier** de chaque marché : **PDF** (1 pièce) ou **ZIP** (plusieurs pièces) | ✅ |
| 12 | **Design pro** : bandeaux dégradés, cartes à accent par secteur, icônes de domaine | ✅ |
| 13 | **Fiche société** (RC, ICE, IF, CNSS, représentant…) enregistrée | ✅ |
| 14 | **Générateur de documents** pré-remplis : acte d'engagement, déclaration sur l'honneur, lettre de maintien de l'offre (PDF, ou ZIP groupé) | ✅ |

### Compatibilité

- **Android 7.0+ (API 24)** — couvre la quasi-totalité du parc.
- Les **notifications fonctionnent sur toutes les versions** : sur Android < 13 elles
  sont actives par défaut ; sur Android 13+ une simple autorisation est demandée.
- **Aucune dépendance à Google Play Services / Firebase** : les alertes reposent sur
  WorkManager + notifications locales, donc l'app fonctionne aussi sur les téléphones
  **sans services Google** (Huawei, etc.).

### À venir (prochaines étapes)
- Documents supplémentaires (notes moyens humains/techniques, méthodologie,
  planning, bordereau, demande de caution)
- Connexion à une source réelle de marchés (API / portail) + résultats publiés
- Abonnements (Gratuit / Standard / Pro) et paiement in-app
- Export PDF du résumé d'un marché

## 🧱 Architecture

Application **native Android** en **Kotlin + Jetpack Compose (Material 3)**, suivant
un découpage MVVM propre :

```
app/src/main/java/com/bmarche/pro/
├── data/
│   ├── model/        # Entités métier (AppelOffre, Societe, Domaine, Checklist…)
│   ├── local/        # Room : favoris + avancement checklist
│   └── repository/   # Repository unique, données d'exemple, profil (DataStore)
├── domain/           # PrixReferenceCalculator (logique de prix, testée)
└── ui/
    ├── theme/        # Couleurs, typographie, thème clair/sombre
    ├── navigation/   # Graphe de navigation + barre d'onglets
    ├── components/   # Composants réutilisables (cartes, badges…)
    └── screens/      # liste, detail, prix, checklist, favoris, societes, profil
```

- **État local** persisté avec **Room** (favoris, checklist) et **DataStore** (profil
  d'alertes).
- **Données de marchés/sociétés** actuellement fournies par `SampleData` ;
  l'architecture est prête à brancher une API distante sans toucher à l'UI.
- **Bilingue FR / AR** : les libellés métier portent leurs traductions, et l'app
  déclare `supportsRtl`.

## 🧮 Calcul du prix de référence

Le moteur (`domain/PrixReferenceCalculator.kt`) est volontairement **transparent**
(pas de boîte noire). Il s'appuie sur des fourchettes de rabais observées par secteur
et retourne :

- le **rabais** de votre prix vs l'estimation ;
- un **positionnement** (trop haut / haut / compétitif / agressif / dangereux) ;
- un **prix de référence** et un **intervalle sûr** ;
- une **estimation de la chance d'être moins-disant**.

Ces fourchettes seront affinées avec les données réelles des résultats de marchés.

## ▶️ Build & exécution

Prérequis : **JDK 17+**, **Android SDK (API 35)**. Ouvrez le projet dans Android
Studio (Ladybug ou plus récent), ou en ligne de commande :

```bash
./gradlew assembleDebug      # génère l'APK debug
./gradlew test               # exécute les tests unitaires (moteur de prix)
./gradlew installDebug       # installe sur un appareil/émulateur connecté
```

> Le premier `./gradlew` télécharge Gradle 8.10.2 puis les dépendances : une
> connexion internet est nécessaire au premier lancement.

## 📦 Stack technique

- Kotlin 2.0 · Jetpack Compose (BOM 2024.12) · Material 3
- Navigation Compose · Lifecycle/ViewModel Compose
- Room · DataStore Preferences · kotlinx.serialization · Coroutines
- Android Gradle Plugin 8.7 · minSdk 26 · targetSdk 35

## 📄 Licence

Projet privé — © 2026 BMarche Pro. Tous droits réservés.
