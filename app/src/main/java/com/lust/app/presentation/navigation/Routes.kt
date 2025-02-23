package com.lust.app.presentation.navigation

sealed class Routes(val route: String) {

    data object MapRoute: Routes("MapRoute")
    data object AddLocationRoute: Routes("AddLocationRoute/{lat}/{lng}") {
        fun createRoute(lat: String, lng: String) = "AddLocationRoute/$lat/$lng"
    }

}