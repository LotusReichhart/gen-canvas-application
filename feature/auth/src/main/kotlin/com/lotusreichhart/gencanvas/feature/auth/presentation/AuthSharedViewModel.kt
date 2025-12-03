package com.lotusreichhart.gencanvas.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lotusreichhart.gencanvas.core.domain.usecase.legal.GetLegalInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class AuthSharedViewModel @Inject constructor(
    private val getLegalInfoUseCase: GetLegalInfoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthSharedUiState())
    val uiState: StateFlow<AuthSharedUiState> = _uiState.asStateFlow()

    init {
        fetchLegalInfo()
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onResetTokenChange(newToken: String) {
        _uiState.update { it.copy(resetToken = newToken) }
    }

    private fun fetchLegalInfo() {
        viewModelScope.launch {
            val result = getLegalInfoUseCase()
            if (result.isSuccess) {
                val info = result.getOrNull()
                _uiState.update {
                    it.copy(
                        termsUrl = info?.termsUrl,
                        privacyUrl = info?.privacyUrl
                    )
                }
                Timber.d("Lấy thông tin pháp lý thành công: $info")
            } else {
                Timber.e("Lấy thông tin pháp lý thất bại: ${result.exceptionOrNull()}")
            }
        }
    }
}