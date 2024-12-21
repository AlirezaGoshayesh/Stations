package ir.bit24.alireza.presentation.navigation;

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ir.bit24.alireza.presentation.features.main.MainVM
import ir.bit24.alireza.presentation.features.detail.DetailScreen
import ir.bit24.alireza.presentation.features.detail.DetailVM
import ir.bit24.alireza.presentation.features.main.MainScreen
import java.util.Map.entry

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        composable(route = Screen.MainScreen.route) {
            val entry = remember(it) {
                navController.getBackStackEntry(Screen.MainScreen.route)
            }
            val mainVM = hiltViewModel<MainVM>(entry)
            MainScreen(navController = navController, viewModel = mainVM)
        }
        composable(
            route = "${Screen.DetailScreen.route}/{stationId}",
            arguments = listOf(navArgument("stationId") {
                type = NavType.LongType
                nullable = false
            })
        ) {
            val stationId = it.arguments?.getLong("stationId")
            if (stationId != null) {
                val entry = remember(it) {
                    navController.getBackStackEntry("${Screen.DetailScreen.route}/{stationId}")
                }
                val mainVM = hiltViewModel<DetailVM>(entry)
                DetailScreen(stationId, mainVM)
            }
        }
    }

}