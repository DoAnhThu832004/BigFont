package com.thupo.bigfont.presentation.screen.splash

import androidx.compose.runtime.Immutable

@Immutable
sealed interface SplashUiState {
    data object Loading: SplashUiState
    data class Error(val errorMessage: String): SplashUiState
}
sealed interface SplashUiEffect {
    data class NavigateNext(val navigationToIntro: Boolean): SplashUiEffect
}