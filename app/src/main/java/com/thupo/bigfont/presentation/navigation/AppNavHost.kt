package com.thupo.bigfont.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.thupo.bigfont.presentation.screen.custom.CustomSizeScreen
import com.thupo.bigfont.presentation.screen.home.HomeScreen
import com.thupo.bigfont.presentation.screen.intro.IntroScreen
import com.thupo.bigfont.presentation.screen.magnifier.MagnifierScreen
import com.thupo.bigfont.presentation.screen.setting.SettingScreen
import com.thupo.bigfont.presentation.screen.splash.SplashScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                viewModel = hiltViewModel(),
                onNavigateNext = { navigationToIntro ->
                    val destination = if (navigationToIntro) Screen.Intro.route else Screen.Home.route
                    navController.navigate(destination) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Intro.route) {
            IntroScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Intro.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToSettings = {
                    navController.navigate(Screen.Setting.route)
                },
                onNavigateToCustomSize = {
                    navController.navigate(Screen.CustomSize.route)
                },
                onNavigateToMagnifier = {
                    navController.navigate(Screen.Magnifier.route)
                }
            )
        }
        composable(Screen.CustomSize.route) {
            CustomSizeScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Setting.route) {
            SettingScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Magnifier.route) {
            MagnifierScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}