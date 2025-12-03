package com.lotusreichhart.gencanvas.feature.account.presentation

import androidx.lifecycle.viewModelScope
import com.lotusreichhart.gencanvas.core.common.event.GlobalUiEventManager
import com.lotusreichhart.gencanvas.core.common.event.UiEvent
import com.lotusreichhart.gencanvas.core.common.util.TextResource
import com.lotusreichhart.gencanvas.core.domain.usecase.auth.SignOutUseCase
import com.lotusreichhart.gencanvas.core.domain.usecase.user.GetProfileStreamUseCase
import com.lotusreichhart.gencanvas.core.domain.util.NetworkMonitor
import com.lotusreichhart.gencanvas.core.ui.viewmodel.AuthenticatedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.lotusreichhart.gencanvas.core.common.R as CoreR
import com.lotusreichhart.gencanvas.feature.account.R

@HiltViewModel
internal class AccountTabViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    globalUiEventManager: GlobalUiEventManager,
    getProfileStreamUseCase: GetProfileStreamUseCase,
    private val signOutUseCase: SignOutUseCase
) : AuthenticatedViewModel(
    networkMonitor = networkMonitor,
    globalUiEventManager = globalUiEventManager,
    getProfileStreamUseCase = getProfileStreamUseCase
) {
    fun onSignOutClick() {
        if (isSignIn) {
            sendUiEvent(
                UiEvent.ShowDialog(
                    title = TextResource.Id(R.string.dialog_sign_out_title),
                    message = TextResource.Id(R.string.dialog_sign_out_message),
                    positiveButtonText = TextResource.Id(CoreR.string.core_action_confirm),
                    onPositiveClick = {
                        viewModelScope.launch {
                            signOutUseCase()
                            sendUiEvent(
                                UiEvent.ShowSnackBar(
                                    message = TextResource.Id(R.string.success_signed_out),
                                    type = UiEvent.SnackBarType.INFO
                                )
                            )
                        }
                    },
                    negativeButtonText = TextResource.Id(CoreR.string.core_action_cancel),
                    onNegativeClick = { }
                )
            )
        }
    }
}