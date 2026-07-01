package com.bmarche.pro.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bmarche.pro.ui.screens.checklist.ChecklistScreen
import com.bmarche.pro.ui.screens.detail.DetailScreen
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
                            icon = { Icon(dest.icon, contentDescription = dest.labelFr) },
                            label = { Text(dest.labelFr) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = TopDestination.LISTE.route,
            modifier = Modifier
        ) {
            composable(TopDestination.LISTE.route) {
                ListeScreen(
                    onOuvrirDetail = { navController.navigate(Routes.detail(it)) },
                    modifier = Modifier.padding(padding)
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
                ProfilScreen(modifier = Modifier.padding(padding))
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
        }
    }
}
