package com.lotusreichhart.gencanvas.feature.auth.presentation.signup

import androidx.lifecycle.viewModelScope
import com.lotusreichhart.gencanvas.core.common.event.GlobalUiEventManager
import com.lotusreichhart.gencanvas.core.domain.usecase.auth.RequestSignUpUseCase
import com.lotusreichhart.gencanvas.core.domain.util.NetworkMonitor
import com.lotusreichhart.gencanvas.core.ui.viewmodel.BaseViewModel
import com.lotusreichhart.gencanvas.feature.auth.presentation.util.AuthErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class SignUpViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    globalUiEventManager: GlobalUiEventManager,
    private val requestSignUpUseCase: RequestSignUpUseCase,
    private val authErrorHandler: AuthErrorHandler
) : BaseViewModel(
    networkMonitor = networkMonitor,
    globalUiEventManager = globalUiEventManager,
) {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun clearEmailErrorMessage() {
        _uiState.update { it.copy(emailErrorMessage = null) }
    }

    fun onNameChange(newValue: String) {
        _uiState.update { it.copy(name = newValue, nameErrorMessage = null) }
    }

    fun onPasswordChange(newValue: String) {
        _uiState.update { it.copy(password = newValue, passwordErrorMessage = null) }
    }

    fun onRequestSignUpClick(email: String) {
        val name = _uiState.value.name
        val password = _uiState.value.password

        Timber.d("onRequestSignUpClick running....: $name $email")

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = requestSignUpUseCase(name, email, password)
            Timber.d("onRequestSignUpClick result: $result")


            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
            } else {
                val exception = result.exceptionOrNull()

                authErrorHandler.handleError(exception) { errors ->
                    _uiState.update {
                        it.copy(
                            nameErrorMessage = errors["name"],
                            emailErrorMessage = errors["email"],
                            passwordErrorMessage = errors["password"]
                        )
                    }
                }

                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun resetSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}