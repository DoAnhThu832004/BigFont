package com.thupo.bigfont.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thupo.bigfont.domain.model.FontScaleItem
import com.thupo.bigfont.domain.usecase.ApplyFontScaleUseCase
import com.thupo.bigfont.domain.usecase.CheckWritePermissionUseCase
import com.thupo.bigfont.domain.usecase.DeleteCustomFontUseCase
import com.thupo.bigfont.domain.usecase.GetFontScalesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFontScalesUseCase: GetFontScalesUseCase,
    private val applyFontScaleUseCase: ApplyFontScaleUseCase,
    private val checkWritePermissionUseCase: CheckWritePermissionUseCase,
    private val deleteCustomFontUseCase: DeleteCustomFontUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _effectChannel = Channel<HomeUiEffect>(Channel.BUFFERED)
    val effectFlow = _effectChannel.receiveAsFlow()

    init {
        loadFontScales()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.RefreshData -> loadFontScales()
            is HomeUiEvent.OnSelectScale -> handleSelectScale(event.item)
            is HomeUiEvent.OnDeleteCustomScale -> handleDeleteCustomScale(event.item)
            is HomeUiEvent.OnDismissPermissionDialog -> {
                _uiState.update { it.copy(showPermissionDialog = false, pendingScaleItem = null) }
            }
            is HomeUiEvent.OnConfirmRequestPermission -> {
                _uiState.update { it.copy(showPermissionDialog = false) }
                viewModelScope.launch {
                    _effectChannel.send(HomeUiEffect.OpenSystemWriteSettings)
                }
            }
            is HomeUiEvent.OnClickCustomSize -> {
                viewModelScope.launch { _effectChannel.send(HomeUiEffect.NavigateToCustomSize) }
            }
            is HomeUiEvent.OnClickMagnifier -> {
                viewModelScope.launch { _effectChannel.send(HomeUiEffect.NavigateToMagnifier) }
            }
        }
    }

    private fun loadFontScales() {
        viewModelScope.launch {
            getFontScalesUseCase().collect { items ->
                _uiState.update { it.copy(fontScales = items) }
            }
        }
    }

    private fun handleDeleteCustomScale(item: FontScaleItem) {
        viewModelScope.launch {
            deleteCustomFontUseCase(item.id)
                .onSuccess {
                    _effectChannel.send(HomeUiEffect.ShowToast("Đã xóa cỡ chữ ${item.title}"))
                }
                .onFailure {
                    _effectChannel.send(HomeUiEffect.ShowToast("Không thể xóa: ${it.message}"))
                }
        }
    }

    private fun handleSelectScale(item: FontScaleItem) {
        if (!checkWritePermissionUseCase()) {
            _uiState.update { it.copy(showPermissionDialog = true, pendingScaleItem = item) }
            return
        }

        viewModelScope.launch {
            applyFontScaleUseCase(item.scale)
                .onSuccess {
                    _effectChannel.send(HomeUiEffect.ShowToast("Đã áp dụng cỡ chữ ${item.title}"))
                    loadFontScales()
                }
                .onFailure {
                    _effectChannel.send(HomeUiEffect.ShowToast("Không thể thay đổi cỡ chữ: ${it.message}"))
                }
        }
    }
}