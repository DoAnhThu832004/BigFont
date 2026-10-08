package com.thupo.bigfont.presentation.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Intro : Screen("intro")
    data object Home : Screen("home")
    data object CustomSize : Screen("custom_size")
    data object Setting : Screen("setting")
    data object Magnifier : Screen("magnifier")
}