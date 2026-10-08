package com.thupo.bigfont.presentation.screen.custom

data class CustomSizeUiState(
    val scale: Float = 1.0f,
    val showPermissionDialog: Boolean = false,
    val isLoading: Boolean = false
) {
    val percentage: Int
        get() = (scale * 100).toInt()
}
sealed interface CustomSizeUiEvent {
    data class OnScaleChanged(val newScale: Float) : CustomSizeUiEvent
    data object OnSaveCustomFont : CustomSizeUiEvent
    data object OnApplyNow : CustomSizeUiEvent
    data object OnConfirmPermission : CustomSizeUiEvent
    data object OnDismissPermissionDialog : CustomSizeUiEvent
}
sealed interface CustomSizeUiEffect {
    data object NavigateBack : CustomSizeUiEffect
    data object OpenSystemWriteSettings : CustomSizeUiEffect
    data class ShowToast(val message: String) : CustomSizeUiEffect
}