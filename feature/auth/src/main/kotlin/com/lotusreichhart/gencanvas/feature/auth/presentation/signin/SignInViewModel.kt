package com.lotusreichhart.gencanvas.feature.auth.presentation.signin

import androidx.lifecycle.viewModelScope
import com.lotusreichhart.gencanvas.core.common.event.GlobalUiEventManager
import com.lotusreichhart.gencanvas.core.common.event.UiEvent
import com.lotusreichhart.gencanvas.core.common.util.TextResource
import com.lotusreichhart.gencanvas.core.domain.usecase.auth.SignInWithEmailUseCase
import com.lotusreichhart.gencanvas.core.domain.usecase.auth.SignInWithGoogleUseCase
import com.lotusreichhart.gencanvas.core.domain.util.NetworkMonitor
import com.lotusreichhart.gencanvas.core.ui.viewmodel.BaseViewModel
import com.lotusreichhart.gencanvas.feature.auth.R
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
internal class SignInViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    globalUiEventManager: GlobalUiEventManager,
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val authErrorHandler: AuthErrorHandler
) : BaseViewModel(
    networkMonitor = networkMonitor,
    globalUiEventManager = globalUiEventManager,
) {
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    fun clearEmailErrorMessage() {
        _uiState.update { it.copy(emailErrorMessage = null) }
    }

    fun onPasswordChange(newValue: String) {
        _uiState.update { it.copy(password = newValue, passwordErrorMessage = null) }
    }

    fun onSignInClick(email: String) {
        val password = _uiState.value.password
        Timber.d("onSignInClick running.... $email")

        viewModelScope.launch {
            _uiState.update { it.copy(loadingType = SignInLoadingType.EMAIL_SIGN_IN) }

            val result = signInWithEmailUseCase(email = email, password = password)
            Timber.d("onSignInClick result: $result")


            if (result.isSuccess) {
                sendUiEvent(
                    UiEvent.ShowSnackBar(
                        TextResource.Id(R.string.sign_in_successful),
                        type = UiEvent.SnackBarType.SUCCESS
                    )
                )
                _uiState.update {
                    it.copy(
                        loadingType = SignInLoadingType.NONE,
                        isSuccess = true
                    )
                }
            } else {
                val exception = result.exceptionOrNull()

                authErrorHandler.handleError(exception) { errors ->
                    _uiState.update {
                        it.copy(
                            emailErrorMessage = errors["email"],
                            passwordErrorMessage = errors["password"]
                        )
                    }
                }

                _uiState.update { it.copy(loadingType = SignInLoadingType.NONE) }
            }
        }
    }

    fun onSignInWithGoogleClick(idToken: String) {
        Timber.d("onSignInWithGoogleClick running....")

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    loadingType = SignInLoadingType.GOOGLE_SIGN_IN,
                )
            }

            val result = signInWithGoogleUseCase(idToken)
            Timber.d("onSignInWithGoogleClick result: $result")

            if (result.isSuccess) {
                sendUiEvent(
                    UiEvent.ShowSnackBar(
                        TextResource.Id(R.string.sign_in_successful),
                        type = UiEvent.SnackBarType.SUCCESS
                    )
                )
                _uiState.update {
                    it.copy(
                        loadingType = SignInLoadingType.NONE,
                        isSuccess = true
                    )
                }
            } else {
                val exception = result.exceptionOrNull()

                authErrorHandler.handleError(exception) {}

                _uiState.update { it.copy(loadingType = SignInLoadingType.NONE) }
            }
        }
    }

    fun onGoogleSignInError(error: TextResource) {
        viewModelScope.launch {
            sendUiEvent(UiEvent.ShowSnackBar(
                message = error,
                type = UiEvent.SnackBarType.ERROR
            ))
        }
    }
}