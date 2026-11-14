package com.campominado.app.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
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

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val factory = remember(context) { AppViewModelFactory(context) }
    var perfil by rememberSaveable { mutableStateOf<String?>(null) }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val items = when (perfil) {
        "Administrador" -> listOf(NavItem(AppRoutes.HOME, "Início"), NavItem(AppRoutes.ADMIN, "Níveis"))
        "Jogador" -> listOf(NavItem(AppRoutes.HOME, "Início"), NavItem(AppRoutes.GAME, "Jogar"), NavItem(AppRoutes.HISTORY, "Histórico"), NavItem(AppRoutes.TIPS, "Dicas"))
        else -> emptyList()
    }

    Scaffold(bottomBar = {
        if (currentRoute != AppRoutes.LOGIN && items.isNotEmpty()) {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = backStackEntry?.destination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = { navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        } },
                        icon = {},
                        label = { Text(item.title) }
                    )
                }
            }
        }
    }) { paddingValues ->
        NavHost(navController, startDestination = AppRoutes.LOGIN, modifier = Modifier.padding(paddingValues)) {
            composable(AppRoutes.LOGIN) {
                LoginScreen(onSelectPerfil = { selecionado ->
                    perfil = selecionado
                    navController.navigate(AppRoutes.HOME) { popUpTo(AppRoutes.LOGIN) { inclusive = true } }
                })
            }
            composable(AppRoutes.HOME) {
                HomeScreen(
                    isAdmin = perfil == "Administrador",
                    onPlayClick = { if (perfil == "Jogador") navController.navigate(AppRoutes.GAME) },
                    onHistoryClick = { if (perfil == "Jogador") navController.navigate(AppRoutes.HISTORY) },
                    onTipsClick = { if (perfil == "Jogador") navController.navigate(AppRoutes.TIPS) },
                    onAdminClick = { if (perfil == "Administrador") navController.navigate(AppRoutes.ADMIN) }
                )
            }
            composable(AppRoutes.GAME) {
                if (perfil == "Jogador") {
                    val vm: GameViewModel = viewModel(factory = factory)
                    MinesweeperScreen(vm)
                } else RedirectHome(navController)
            }
            composable(AppRoutes.HISTORY) {
                if (perfil == "Jogador") {
                    val vm: HistoryViewModel = viewModel(factory = factory)
                    HistoryScreen(vm)
                } else RedirectHome(navController)
            }
            composable(AppRoutes.TIPS) {
                if (perfil == "Jogador") {
                    val vm: AdminViewModel = viewModel(factory = factory)
                    TipsScreen(vm)
                } else RedirectHome(navController)
            }
            composable(AppRoutes.ADMIN) {
                if (perfil == "Administrador") {
                    val vm: AdminViewModel = viewModel(factory = factory)
                    AdminScreen(vm)
                } else RedirectHome(navController)
            }
        }
    }
}

@Composable
private fun LoginScreen(onSelectPerfil: (String) -> Unit) {
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Campo Minado", modifier = Modifier.padding(bottom = 24.dp))
        Button(onClick = { onSelectPerfil("Jogador") }) { Text("Entrar como Jogador") }
        Button(onClick = { onSelectPerfil("Administrador") }, modifier = Modifier.padding(top = 12.dp)) { Text("Entrar como Administrador") }
    }
}

@Composable
private fun RedirectHome(navController: androidx.navigation.NavHostController) {
    LaunchedEffect(Unit) {
        navController.navigate(AppRoutes.HOME) { popUpTo(navController.graph.findStartDestination().id) }
    }
}
