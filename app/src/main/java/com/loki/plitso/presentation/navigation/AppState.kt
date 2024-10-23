package com.loki.plitso.presentation.navigation

import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController

@Stable
class AppState(
    val navController: NavHostController,
) {
    fun navigateUp() {
        navController.navigateUp()
    }

    fun navigate(screen: Screen) {
        val route =
            screen.routePath?.let { routePath ->
                screen.route + "/$routePath"
            } ?: screen.route

        navController.navigate(route = route) {
            launchSingleTop = true
            restoreState = screen.restoreState
        }
    }

    fun bottomNavNavigate(route: String) {
        navController.navigate(route = route) {
            launchSingleTop = true
            popUpTo(Screen.HomeScreen.route) {
                saveState = true
            }
            restoreState = true
        }
    }
}
