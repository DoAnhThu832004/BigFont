package com.thupo.bigfont.presentation.navigation

sealed class Screen (val route: String) {
    object Splash : Screen("splash")
    object Intro : Screen("intro")
    object Home : Screen("home")
    object CustomSize : Screen("custom_size")
}