package com.lotusreichhart.gencanvas.core.common.event

import com.lotusreichhart.gencanvas.core.common.event.UiEvent
import com.lotusreichhart.gencanvas.core.common.util.TextResource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalUiEventManager @Inject constructor() {
    private val _events = MutableSharedFlow<UiEvent>(replay = 1)
    val events = _events.asSharedFlow()

    suspend fun showToast(message: String) {
        _events.emit(UiEvent.ShowToast(TextResource.Raw(message)))
    }

    suspend fun showToast(message: TextResource) {
        _events.emit(UiEvent.ShowToast(message))
    }

    suspend fun showSnackBar(
        message: String,
        type: UiEvent.SnackBarType = UiEvent.SnackBarType.INFO
    ) {
        _events.emit(UiEvent.ShowSnackBar(TextResource.Raw(message), null, type))
    }

    suspend fun showSnackBar(
        message: TextResource,
        type: UiEvent.SnackBarType = UiEvent.SnackBarType.INFO
    ) {
        _events.emit(UiEvent.ShowSnackBar(message, null, type))
    }

    suspend fun showDialog(
        title: TextResource,
        message: TextResource,
        positiveButtonText: TextResource = TextResource.Raw("OK"),
        onPositiveClick: () -> Unit = {},
        negativeButtonText: TextResource? = null,
        onNegativeClick: (() -> Unit)? = null
    ) {
        _events.emit(
            value = UiEvent.ShowDialog(
                title = title,
                message = message,
                positiveButtonText = positiveButtonText,
                onPositiveClick = onPositiveClick,
                negativeButtonText = negativeButtonText,
                onNegativeClick = onNegativeClick
            )
        )
    }

    suspend fun showDialog(
        title: String,
        message: String,
        positiveText: String = "OK",
        onPositiveClick: () -> Unit = {},
        negativeText: String? = null,
        onNegativeClick: (() -> Unit)? = null
    ) {
        _events.emit(
            value = UiEvent.ShowDialog(
                title = TextResource.Raw(title),
                message = TextResource.Raw(message),
                positiveButtonText = TextResource.Raw(positiveText),
                onPositiveClick = onPositiveClick,
                negativeButtonText = if (negativeText != null) TextResource.Raw(negativeText) else null,
                onNegativeClick = onNegativeClick
            )
        )
    }

    suspend fun navigate(route: String) {
        _events.emit(UiEvent.Navigate(route))
    }
}