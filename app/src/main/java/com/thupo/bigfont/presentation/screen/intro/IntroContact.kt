package com.thupo.bigfont.presentation.screen.intro

sealed interface IntroUiEffect {
    data object NavigateToHome: IntroUiEffect
}