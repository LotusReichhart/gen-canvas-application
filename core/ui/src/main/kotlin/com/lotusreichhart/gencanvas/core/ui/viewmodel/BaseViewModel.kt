package com.lotusreichhart.gencanvas.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lotusreichhart.gencanvas.core.common.event.GlobalUiEventManager
import com.lotusreichhart.gencanvas.core.common.event.UiEvent
import com.lotusreichhart.gencanvas.core.common.util.TextResource
import com.lotusreichhart.gencanvas.core.domain.util.NetworkMonitor
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import com.lotusreichhart.gencanvas.core.common.R

open class BaseViewModel @Inject constructor(
    val networkMonitor: NetworkMonitor,
    private val globalUiEventManager: GlobalUiEventManager,
) : ViewModel() {
    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = true
        )

    init {
        setupNetworkMonitoring()
    }

    private fun setupNetworkMonitoring() {
        Timber.d("Bắt đầu giám sát mạng...")
        viewModelScope.launch {

            var isFirstValue = true

            networkMonitor.isOnline.collect { isOnline ->
                if (isFirstValue) {
                    if (!isOnline) {
                        Timber.d("Trạng thái ban đầu: Offline. Báo Snackbar.")
                        globalUiEventManager.showSnackBar(
                            message = TextResource.Id(R.string.core_error_no_internet),
                            type = UiEvent.SnackBarType.ERROR
                        )
                    } else {
                        Timber.d("Trạng thái ban đầu: Online. Không báo.")
                    }
                    isFirstValue = false
                } else {
                    if (isOnline) {
                        Timber.d("Trạng thái thay đổi: Online. Báo SUCCESS.")
                        globalUiEventManager.showSnackBar(
                            message = TextResource.Id(R.string.core_success_internet_restored),
                            type = UiEvent.SnackBarType.SUCCESS
                        )
                    } else {
                        Timber.d("Trạng thái thay đổi: Offline. Báo ERROR.")
                        globalUiEventManager.showSnackBar(
                            message = TextResource.Id(R.string.core_error_no_internet),
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