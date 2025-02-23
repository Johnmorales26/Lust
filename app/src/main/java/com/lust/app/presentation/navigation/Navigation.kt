package com.lust.app.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lust.app.presentation.addLocationModule.AddLocationScreen
import com.lust.app.presentation.mapModule.view.MapScreen

val LocalNavController =
    staticCompositionLocalOf<NavController> { error("No NavController provided") }

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = Routes.MapRoute.route
        ) {
            addMapScreen()
            addLocationScreen()
        }
    }
}

fun NavGraphBuilder.addMapScreen() {
    composable(route = Routes.MapRoute.route) {
        MapScreen(modifier = Modifier.fillMaxSize())
    }
}

fun NavGraphBuilder.addLocationScreen() {
    composable(
        route = Routes.AddLocationRoute.route,
        arguments = listOf(
            navArgument("lat") { type = NavType.StringType },
            navArgument("lng") { type = NavType.StringType })
    ) { backStackEntry ->
        val lat = backStackEntry.arguments?.getString("lat") ?: "0.0"
        val lng = backStackEntry.arguments?.getString("lng") ?: "0.0"
        AddLocationScreen(
            modifier = Modifier.fillMaxSize(),
            lat = lat.toDouble(),
            lng = lng.toDouble()
        )
    }
}