package com.lotusreichhart.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lotusreichhart.core.ui.event.GlobalUiEventManager
import com.lotusreichhart.core.ui.event.UiEvent
import com.lotusreichhart.core.utils.logD
import com.lotusreichhart.domain.entity.UserEntity
import com.lotusreichhart.domain.monitor.NetworkMonitor
import com.lotusreichhart.domain.usecase.user.GetProfileStreamUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

open class BaseViewModel @Inject constructor(
    val networkMonitor: NetworkMonitor,
    private val globalUiEventManager: GlobalUiEventManager,
    getProfileStreamUseCase: GetProfileStreamUseCase
) : ViewModel() {

    val currentUser: StateFlow<UserEntity?> = getProfileStreamUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    val isLoggedIn: Boolean
        get() = currentUser.value != null

    init {
        setupNetworkMonitoring()
    }

    private fun setupNetworkMonitoring() {
        logD("Bắt đầu giám sát mạng...")
        viewModelScope.launch {

            var isFirstValue = true

            networkMonitor.isOnline.collect { isOnline ->
                if (isFirstValue) {
                    if (!isOnline) {
                        logD("Trạng thái ban đầu: Offline. Báo Snackbar.")
                        globalUiEventManager.showSnackBar(
                            "Không có kết nối internet",
                            type = UiEvent.SnackBarType.ERROR
                        )
                    } else {
                        logD("Trạng thái ban đầu: Online. Không báo.")
                    }
                    isFirstValue = false
                } else {
                    if (isOnline) {
                        logD("Trạng thái thay đổi: Online. Báo SUCCESS.")
                        globalUiEventManager.showSnackBar(
                            "Internet đã được khôi phục",
                            type = UiEvent.SnackBarType.SUCCESS
                        )
                    } else {
                        logD("Trạng thái thay đổi: Offline. Báo ERROR.")
                        globalUiEventManager.showSnackBar(
                            "Không có kết nối internet",
                            type = UiEvent.SnackBarType.ERROR
                        )
                    }
                }
            }
        }
    }

    protected fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            when (event) {
                is UiEvent.ShowSnackBar -> globalUiEventManager.showSnackBar(
                    event.message,
                    event.actionLabel,
                    event.type
                )

                is UiEvent.ShowToast -> globalUiEventManager.showToast(
                    event.message
                )

                is UiEvent.ShowDialog -> globalUiEventManager.showDialog(
                    event.title,
                    event.message,
                    event.positiveButtonText,
                    event.onPositiveClick,
                    event.negativeButtonText,
                    event.onNegativeClick
                )

                is UiEvent.Navigate -> globalUiEventManager.navigate(
                    event.route
                )

                // FCM
            }
        }
    }
}