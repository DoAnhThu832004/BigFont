package com.thupo.bigfont.presentation.screen.splash

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thupo.bigfont.domain.usecase.PrepareInitialDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val prepareInitialDataUseCase: PrepareInitialDataUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    private val _effectChannel = Channel<SplashUiEffect>(Channel.BUFFERED)
    val effectFlow = _effectChannel.receiveAsFlow()

    private var hasStarted = false

    init {
        startPreparation()
    }
    fun startPreparation() {
        if (hasStarted) return
        hasStarted = true
        viewModelScope.launch {
            _uiState.value = SplashUiState.Loading
            val startMs = SystemClock.elapsedRealtime()
            val result = withContext(Dispatchers.IO) {
                prepareInitialDataUseCase()
            }
            val elapsed = SystemClock.elapsedRealtime() - startMs
            val minDisplayTime = 3000L
            if (elapsed < minDisplayTime) {
                delay(minDisplayTime - elapsed)
            }
            result.fold(
                onSuccess = { navigationToIntro ->
                    _effectChannel.send(SplashUiEffect.NavigateNext(navigationToIntro))
                },
                onFailure = {
                    _uiState.value = SplashUiState.Error(it.message ?: "Unknown error")
                }
            )
        }
    }
}