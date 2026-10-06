package com.thupo.bigfont.presentation.navigation

sealed class Screen (val route: String) {
    object Splash : Screen("splash")
}