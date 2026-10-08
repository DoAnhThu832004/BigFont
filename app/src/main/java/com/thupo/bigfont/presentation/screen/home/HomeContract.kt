package com.thupo.bigfont.presentation.screen.home

import com.thupo.bigfont.domain.model.FontScaleItem

data class HomeUiState(
    val fontScales: List<FontScaleItem> = emptyList(),
    val currentScale: Float = 1.0f,
    val isLoading: Boolean = false,
    val showPermissionDialog: Boolean = false,
    val pendingScaleItem: FontScaleItem? = null
)

sealed interface HomeUiEvent {
    data object RefreshData : HomeUiEvent
    data class OnSelectScale(val item: FontScaleItem) : HomeUiEvent
    data class OnDeleteCustomScale(val item: FontScaleItem) : HomeUiEvent
    data object OnDismissPermissionDialog : HomeUiEvent
    data object OnConfirmRequestPermission : HomeUiEvent
    data object OnClickCustomSize : HomeUiEvent
    data object OnClickMagnifier : HomeUiEvent
}

sealed interface HomeUiEffect {
    data object OpenSystemWriteSettings : HomeUiEffect
    data class ShowToast(val message: String) : HomeUiEffect
    data object NavigateToCustomSize : HomeUiEffect
    data object NavigateToMagnifier : HomeUiEffect
}