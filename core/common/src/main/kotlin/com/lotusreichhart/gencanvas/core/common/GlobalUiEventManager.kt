package com.lotusreichhart.gencanvas.core.common

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalUiEventManager  @Inject constructor()  {
    private val _events = MutableSharedFlow<UiEvent>(replay = 1)
    val events = _events.asSharedFlow()

    suspend fun showToast(message: String) {
        _events.emit(UiEvent.ShowToast(message))
    }

    suspend fun showSnackBar(
        message: String,
        actionLabel: String? = null,
        type: UiEvent.SnackBarType = UiEvent.SnackBarType.INFO
    ) {
        _events.emit(UiEvent.ShowSnackBar(message, actionLabel, type))
    }

    suspend fun showDialog(
        title: String,
        message: String,
        positiveButtonText: String = "OK",
        onPositiveClick: () -> Unit = {},
        negativeButtonText: String? = null,
        onNegativeClick: (() -> Unit)? = null
    ) {
        _events.emit(
            UiEvent.ShowDialog(
                title = title,
                message = message,
                positiveButtonText = positiveButtonText,
                onPositiveClick = onPositiveClick,
                negativeButtonText = negativeButtonText,
                onNegativeClick = onNegativeClick
            )
        )
    }

    suspend fun navigate(route: String) {
        _events.emit(UiEvent.Navigate(route))
    }
}