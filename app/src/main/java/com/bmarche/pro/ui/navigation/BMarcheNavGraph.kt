package com.bmarche.pro.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bmarche.pro.data.model.Region
import com.bmarche.pro.data.model.TypePublication
import com.bmarche.pro.ui.label
import com.bmarche.pro.ui.screens.accueil.AccueilScreen
import com.bmarche.pro.ui.screens.checklist.ChecklistScreen
import com.bmarche.pro.ui.screens.detail.DetailScreen
import com.bmarche.pro.ui.screens.documents.DocumentsScreen
import com.bmarche.pro.ui.screens.masociete.MaSocieteScreen
import com.bmarche.pro.ui.screens.favoris.FavorisScreen
import com.bmarche.pro.ui.screens.liste.ListeScreen
import com.bmarche.pro.ui.screens.prix.PrixScreen
import com.bmarche.pro.ui.screens.profil.ProfilScreen
import com.bmarche.pro.ui.screens.societes.SocieteDetailScreen
import com.bmarche.pro.ui.screens.societes.SocietesScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMarcheApp(navController: NavHostController = rememberNavController()) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Les routes peuvent porter des arguments optionnels (ex: "prix?aoId=…") : on
    // compare donc sur la base de la route, avant le "?".
    val baseRoute = backStackEntry?.destination?.route?.substringBefore('?')
    val topDest = TopDestination.entries.firstOrNull { it.route == baseRoute }
    val estOnglet = topDest != null

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Navigation vers un onglet avec le même comportement que la barre du bas.
    fun ouvrirOnglet(dest: TopDestination) {
        navController.navigate(dest.route) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = estOnglet,
        drawerContent = {
            AppDrawerContent(
                onFermer = { scope.launch { drawerState.close() } },
                onOuvrirOnglet = { dest ->
                    scope.launch { drawerState.close() }
                    ouvrirOnglet(dest)
                },
                onOuvrirType = { type ->
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.liste(type = type.name))
                },
                onOuvrirMaSociete = {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.MA_SOCIETE)
                }
            )
        }
    ) {
    Scaffold(
        bottomBar = {
            if (estOnglet) {
                NavigationBar {
                    val hierarchie = backStackEntry?.destination?.hierarchy
                    TopDestination.entries.forEach { dest ->
                        NavigationBarItem(
                            selected = hierarchie?.any { it.route?.substringBefore('?') == dest.route } == true,
                            onClick = {
                                navController.navigate(dest.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(dest.icon, contentDescription = dest.label()) },
                            label = { Text(dest.label()) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = TopDestination.ACCUEIL.route,
            modifier = Modifier
        ) {
            composable(TopDestination.ACCUEIL.route) {
                AccueilScreen(
                    onOuvrirRegion = { region ->
                        navController.navigate(Routes.liste(region = region.name, type = TypePublication.MARCHE_PUBLIC.name))
                    },
                    onOuvrirTous = { navController.navigate(Routes.liste()) },
                    onOuvrirMenu = { scope.launch { drawerState.open() } },
                    modifier = Modifier.padding(padding)
                )
            }
            composable(
                route = Routes.LISTE,
                arguments = listOf(
                    navArgument("region") { type = NavType.StringType; nullable = true; defaultValue = null },
                    navArgument("type") { type = NavType.StringType; nullable = true; defaultValue = null }
                )
            ) { entry ->
                val region = entry.arguments?.getString("region")
                    ?.let { name -> runCatching { Region.valueOf(name) }.getOrNull() }
                val type = entry.arguments?.getString("type")
                    ?.let { name -> runCatching { TypePublication.valueOf(name) }.getOrNull() }
                val titre = type?.label() ?: region?.label()
                    ?: androidx.compose.ui.res.stringResource(com.bmarche.pro.R.string.liste_titre_tous)
                ListeScreen(
                    region = region,
                    type = type,
                    titre = titre,
                    onRetour = { navController.popBackStack() },
                    onOuvrirDetail = { navController.navigate(Routes.detail(it)) }
                )
            }
            composable(TopDestination.FAVORIS.route) {
                FavorisScreen(
                    onOuvrirDetail = { navController.navigate(Routes.detail(it)) },
                    modifier = Modifier.padding(padding)
                )
            }
            composable(TopDestination.SOCIETES.route) {
                SocietesScreen(
                    onOuvrirSociete = { navController.navigate("societe/$it") },
                    modifier = Modifier.padding(padding)
                )
            }
            composable(
                route = Routes.PRIX_POUR,
                arguments = listOf(navArgument("aoId") {
                    type = NavType.StringType; nullable = true; defaultValue = null
                })
            ) { entry ->
                PrixScreen(
                    aoId = entry.arguments?.getString("aoId"),
                    modifier = Modifier.padding(padding)
                )
            }
            composable(TopDestination.PROFIL.route) {
                ProfilScreen(
                    onOuvrirMaSociete = { navController.navigate(Routes.MA_SOCIETE) },
                    modifier = Modifier.padding(padding)
                )
            }

            composable(
                route = Routes.DETAIL,
                arguments = listOf(navArgument("aoId") { type = NavType.StringType })
            ) { entry ->
                val aoId = entry.arguments?.getString("aoId").orEmpty()
                DetailScreen(
                    aoId = aoId,
                    onRetour = { navController.popBackStack() },
                    onOuvrirChecklist = { navController.navigate(Routes.checklist(it)) },
                    onOuvrirPrix = { navController.navigate(Routes.prixPour(it)) },
                    onOuvrirDocuments = { navController.navigate(Routes.documents(it)) },
                    onOuvrirSociete = { navController.navigate("societe/$it") }
                )
            }
            composable(
                route = Routes.CHECKLIST,
                arguments = listOf(navArgument("aoId") { type = NavType.StringType })
            ) { entry ->
                ChecklistScreen(
                    aoId = entry.arguments?.getString("aoId").orEmpty(),
                    onRetour = { navController.popBackStack() }
                )
            }
            composable(
                route = "societe/{societeId}",
                arguments = listOf(navArgument("societeId") { type = NavType.StringType })
            ) { entry ->
                SocieteDetailScreen(
                    societeId = entry.arguments?.getString("societeId").orEmpty(),
                    onRetour = { navController.popBackStack() }
                )
            }
            composable(Routes.MA_SOCIETE) {
                MaSocieteScreen(onRetour = { navController.popBackStack() })
            }
            composable(
                route = Routes.DOCUMENTS,
                arguments = listOf(navArgument("aoId") { type = NavType.StringType })
            ) { entry ->
                DocumentsScreen(
                    aoId = entry.arguments?.getString("aoId").orEmpty(),
                    onRetour = { navController.popBackStack() },
                    onOuvrirMaSociete = { navController.navigate(Routes.MA_SOCIETE) }
                )
            }
        }
    }
    }
}
