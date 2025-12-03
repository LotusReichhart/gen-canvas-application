package com.lotusreichhart.gencanvas.feature.account.presentation.profile.edit

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.lotusreichhart.gencanvas.core.common.event.GlobalUiEventManager
import com.lotusreichhart.gencanvas.core.common.event.UiEvent
import com.lotusreichhart.gencanvas.core.common.util.TextResource
import com.lotusreichhart.gencanvas.core.domain.usecase.user.GetProfileStreamUseCase
import com.lotusreichhart.gencanvas.core.domain.util.NetworkMonitor
import com.lotusreichhart.gencanvas.core.ui.viewmodel.AuthenticatedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class EditProfileViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    globalUiEventManager: GlobalUiEventManager,
    getProfileStreamUseCase: GetProfileStreamUseCase,
) : AuthenticatedViewModel(
    networkMonitor = networkMonitor,
    globalUiEventManager = globalUiEventManager,
    getProfileStreamUseCase = getProfileStreamUseCase
) {
    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
        Timber.d("Chạy vào Init: $user")
        viewModelScope.launch {
            val user = user.filterNotNull().first()
            _uiState.update {
                it.copy(
                    name = user.name,
                    originalName = user.name,
                    originalAvatarUrl = user.avatarUrl
                )
            }
        }
    }

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName, nameErrorMessage = null) }
    }

    fun onAvatarSelected(uri: Uri?) {
        if (uri != null) {
            _uiState.update { it.copy(avatarUri = uri) }
        }
    }

    fun onSaveClick() {
        val currentState = _uiState.value
        val name = currentState.name.trim()
        val avatarUri = currentState.avatarUri

        if (!currentState.isChanged) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            sendUiEvent(
                UiEvent.ShowSnackBar(
                    TextResource.Raw("Cập nhật thành công"),
                    type = UiEvent.SnackBarType.SUCCESS
                )
            )
            _uiState.update { it.copy(isLoading = false) }

//            val result = updateProfileUseCase(
//                name = name,
//                avatarUri = avatarUri?.toString()
//            )
//
//            if (result.isSuccess) {
//                sendUiEvent(
//                    UiEvent.ShowSnackBar(
//                        "Cập nhật thành công!",
//                        type = UiEvent.SnackBarType.SUCCESS
//                    )
//                )
//                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
//            } else {
//                val errorMsg = result.exceptionOrNull()?.message ?: "Cập nhật thất bại"
//                _uiState.update { it.copy(isLoading = false) }
//                sendUiEvent(
//                    UiEvent.ShowSnackBar(
//                        errorMsg,
//                        type = UiEvent.SnackBarType.ERROR
//                    )
//                )
//            }
        }
    }
}