package com.campominado.app.navigation

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.campominado.app.MinesweeperScreen
import com.campominado.app.ui.AdminScreen
import com.campominado.app.ui.HistoryScreen
import com.campominado.app.ui.HomeScreen
import com.campominado.app.ui.TipsScreen
import com.campominado.app.viewmodel.AdminViewModel
import com.campominado.app.viewmodel.AppViewModelFactory
import com.campominado.app.viewmodel.GameViewModel
import com.campominado.app.viewmodel.HistoryViewModel

object AppRoutes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val GAME = "game"
    const val HISTORY = "history"
    const val ADMIN = "admin"
    const val TIPS = "tips"
}

private data class NavItem(val route: String, val title: String)

private val bottomItems = listOf(
    NavItem(AppRoutes.HOME, "Início"),
    NavItem(AppRoutes.GAME, "Jogar"),
    NavItem(AppRoutes.HISTORY, "Histórico"),
    NavItem(AppRoutes.ADMIN, "Admin"),
    NavItem(AppRoutes.TIPS, "Dicas")
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val factory = AppViewModelFactory(context)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != AppRoutes.LOGIN) {
                NavigationBar {
                    bottomItems.forEach { item ->
                        NavigationBarItem(
                            selected = backStackEntry?.destination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {},
                            label = { Text(item.title) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AppRoutes.LOGIN,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(AppRoutes.LOGIN) {
                androidx.compose.foundation.layout.Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                ) {
                    Text("Campo Minado - Login")
                    androidx.compose.material3.Button(onClick = {
                        navController.navigate(AppRoutes.HOME) {
                            popUpTo(AppRoutes.LOGIN) { inclusive = true }
                        }
                    }) { Text("Entrar") }
                }
            }
            composable(AppRoutes.HOME) {
                HomeScreen(
                    onPlayClick = { navController.navigate(AppRoutes.GAME) },
                    onHistoryClick = { navController.navigate(AppRoutes.HISTORY) },
                    onTipsClick = { navController.navigate(AppRoutes.TIPS) }
                )
            }
            composable(AppRoutes.GAME) {
                val vm: GameViewModel = viewModel(factory = factory)
                MinesweeperScreen(viewModel = vm)
            }
            composable(AppRoutes.HISTORY) {
                val vm: HistoryViewModel = viewModel(factory = factory)
                HistoryScreen(viewModel = vm)
            }
            composable(AppRoutes.ADMIN) {
                val vm: AdminViewModel = viewModel(factory = factory)
                AdminScreen(adminViewModel = vm)
            }
            composable(AppRoutes.TIPS) { TipsScreen() }
        }
    }
}
