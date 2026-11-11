package com.campominado.app.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.campominado.app.MinesweeperScreen
import com.campominado.app.viewmodel.GameViewModel
import com.campominado.app.viewmodel.PartidaResultSaver
import com.campominado.app.viewmodel.PartidaResultado

object AppRoutes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val GAME = "game"
    const val HISTORY = "history"
    const val ADMIN = "admin"
    const val TIPS = "tips"
}

private data class NavItem(
    val route: String,
    val title: String
)

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
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val showBottomBar = currentRoute != AppRoutes.LOGIN

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomItems.forEach { item ->
                        NavigationBarItem(
                            selected = backStackEntry
                                ?.destination
                                ?.hierarchy
                                ?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(
                                        navController.graph.findStartDestination().id
                                    ) {
                                        saveState = true
                                    }
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
                PlaceholderScreen(
                    title = "Campo Minado - Login",
                    buttonText = "Entrar",
                    onClick = {
                        navController.navigate(AppRoutes.HOME) {
                            popUpTo(AppRoutes.LOGIN) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(AppRoutes.HOME) {
                PlaceholderScreen(
                    title = "Tela Inicial",
                    buttonText = "Começar jogo",
                    onClick = {
                        navController.navigate(AppRoutes.GAME)
                    }
                )
            }

            composable(AppRoutes.GAME) {
                val gameViewModel: GameViewModel = viewModel(
                    factory = GameViewModelFactory
                )

                MinesweeperScreen(viewModel = gameViewModel)
            }

            composable(AppRoutes.HISTORY) {
                PlaceholderScreen(
                    title = "Histórico de Partidas",
                    buttonText = "Voltar ao início",
                    onClick = {
                        navController.navigate(AppRoutes.HOME)
                    }
                )
            }

            composable(AppRoutes.ADMIN) {
                PlaceholderScreen(
                    title = "Administração",
                    buttonText = "Voltar ao início",
                    onClick = {
                        navController.navigate(AppRoutes.HOME)
                    }
                )
            }

            composable(AppRoutes.TIPS) {
                PlaceholderScreen(
                    title = "Dicas",
                    buttonText = "Voltar ao início",
                    onClick = {
                        navController.navigate(AppRoutes.HOME)
                    }
                )
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(
    title: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(PaddingValues(24.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = title)

        Button(
            onClick = onClick,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = buttonText)
        }
    }
}

private object GameViewModelFactory : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(
        modelClass: Class<T>
    ): T {
        val noOpSaver = object : PartidaResultSaver {
            override suspend fun salvarResultado(resultado: PartidaResultado) {
                // A integração com Room vai ser feita dps
            }
        }

        return GameViewModel(noOpSaver) as T
    }
}