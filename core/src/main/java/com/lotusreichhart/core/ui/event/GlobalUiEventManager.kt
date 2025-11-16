package com.lotusreichhart.core.ui.event

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalUiEventManager @Inject constructor() {
    private val _events = MutableSharedFlow<UiEvent>(replay = 0)
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
        onConfirm: () -> Unit = {}
    ) {
        _events.emit(UiEvent.ShowDialog(title, message, positiveButtonText, onConfirm))
    }
}