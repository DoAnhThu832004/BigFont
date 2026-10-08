package com.thupo.bigfont.presentation.screen.custom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thupo.bigfont.domain.usecase.ApplyFontScaleUseCase
import com.thupo.bigfont.domain.usecase.CheckWritePermissionUseCase
import com.thupo.bigfont.domain.usecase.SaveCustomFontUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomSizeViewModel @Inject constructor(
    private val applyFontScaleUseCase: ApplyFontScaleUseCase,
    private val checkWritePermissionUseCase: CheckWritePermissionUseCase,
    private val saveCustomFontUseCase: SaveCustomFontUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CustomSizeUiState())
    val uiState: StateFlow<CustomSizeUiState> = _uiState.asStateFlow()

    private val _effectChannel = Channel<CustomSizeUiEffect>(Channel.BUFFERED)
    val effectFlow = _effectChannel.receiveAsFlow()

    fun onEvent(event: CustomSizeUiEvent) {
        when (event) {
            is CustomSizeUiEvent.OnScaleChanged -> {
                _uiState.update { it.copy(scale = event.newScale) }
            }
            is CustomSizeUiEvent.OnApplyNow -> handleApplyNow()
            is CustomSizeUiEvent.OnSaveCustomFont -> handleSaveCustomFont()
            is CustomSizeUiEvent.OnConfirmPermission -> {
                _uiState.update { it.copy(showPermissionDialog = false) }
                viewModelScope.launch {
                    _effectChannel.send(CustomSizeUiEffect.OpenSystemWriteSettings)
                }
            }
            is CustomSizeUiEvent.OnDismissPermissionDialog -> {
                _uiState.update { it.copy(showPermissionDialog = false) }
            }
        }
    }

    private fun handleApplyNow() {
        val currentScale = _uiState.value.scale
        if (!checkWritePermissionUseCase()) {
            _uiState.update { it.copy(showPermissionDialog = true) }
            return
        }
        viewModelScope.launch {
            // Tự động lưu cỡ chữ tùy chỉnh vào danh sách
            val title = "${_uiState.value.percentage}% - Tự chọn (${String.format("%.1fx", currentScale)})"
            saveCustomFontUseCase(currentScale, title)

            applyFontScaleUseCase(currentScale)
                .onSuccess {
                    _effectChannel.send(CustomSizeUiEffect.ShowToast("Đã áp dụng cỡ chữ thành công!"))
                    _effectChannel.send(CustomSizeUiEffect.NavigateBack)
                }
                .onFailure {
                    _effectChannel.send(CustomSizeUiEffect.ShowToast("Không thể thay đổi cỡ chữ: ${it.message}"))
                }
        }
    }

    private fun handleSaveCustomFont() {
        val currentScale = _uiState.value.scale
        val title = "${_uiState.value.percentage}% - Tự chọn (${String.format("%.1fx", currentScale)})"
        viewModelScope.launch {
            saveCustomFontUseCase(currentScale, title)
                .onSuccess {
                    _effectChannel.send(CustomSizeUiEffect.ShowToast("Đã lưu cỡ chữ ${_uiState.value.percentage}%"))
                    _effectChannel.send(CustomSizeUiEffect.NavigateBack)
                }
                .onFailure {
                    _effectChannel.send(CustomSizeUiEffect.ShowToast("Lỗi khi lưu cỡ chữ: ${it.message}"))
                }
        }
    }
}