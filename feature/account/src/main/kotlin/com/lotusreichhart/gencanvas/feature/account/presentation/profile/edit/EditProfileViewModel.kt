package com.lotusreichhart.gencanvas.feature.account.presentation.profile.edit

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.lotusreichhart.gencanvas.core.common.event.GlobalUiEventManager
import com.lotusreichhart.gencanvas.core.common.event.UiEvent
import com.lotusreichhart.gencanvas.core.common.util.TextResource
import com.lotusreichhart.gencanvas.core.data.network.util.ServerException
import com.lotusreichhart.gencanvas.core.domain.repository.EditingRepository
import com.lotusreichhart.gencanvas.core.domain.usecase.user.GetProfileStreamUseCase
import com.lotusreichhart.gencanvas.core.domain.usecase.user.UpdateUserProfileUseCase
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
import java.io.File
import javax.inject.Inject

import com.lotusreichhart.gencanvas.core.common.R as CoreR
import com.lotusreichhart.gencanvas.feature.account.R

@HiltViewModel
internal class EditProfileViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    globalUiEventManager: GlobalUiEventManager,
    getProfileStreamUseCase: GetProfileStreamUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val editingRepository: EditingRepository
) : AuthenticatedViewModel(
    networkMonitor = networkMonitor,
    globalUiEventManager = globalUiEventManager,
    getProfileStreamUseCase = getProfileStreamUseCase
) {
    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    init {
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

    fun onSaveClick(avatarFile: File?) {
        val currentState = _uiState.value
        val name = currentState.name.trim()

        if (!currentState.isChanged) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = updateUserProfileUseCase(name = name, avatarFile = avatarFile)

            if (result.isSuccess) {
                sendUiEvent(
                    UiEvent.ShowSnackBar(
                        TextResource.Id(R.string.profile_update_success),
                        type = UiEvent.SnackBarType.SUCCESS
                    )
                )
                editingRepository.clearSession()
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } else {
                val exception = result.exceptionOrNull()

                val errorResource = if (exception is ServerException) {
                    exception.textResource
                } else {
                    exception?.message?.let { TextResource.Raw(it) }
                        ?: TextResource.Id(CoreR.string.core_unknow_error)
                }

                _uiState.update { it.copy(isLoading = false) }

                sendUiEvent(
                    UiEvent.ShowSnackBar(
                        message = errorResource,
                        type = UiEvent.SnackBarType.ERROR
                    )
                )
            }
        }
    }
}