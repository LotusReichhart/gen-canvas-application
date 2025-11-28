package com.lotusreichhart.gencanvas.core.common

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()

    enum class SnackBarType {
        INFO,
        SUCCESS,
        WARNING,
        ERROR
    }

    data class ShowSnackBar(
        val message: String,
        val actionLabel: String? = null,
        val type: SnackBarType = SnackBarType.INFO
    ) : UiEvent()

    data class ShowDialog(
        val title: String,
        val message: String,

        val positiveButtonText: String = "OK",
        val onPositiveClick: () -> Unit = {},

        val negativeButtonText: String? = null,
        val onNegativeClick: (() -> Unit)? = null
    ) : UiEvent()

    data class Navigate(val route: String) : UiEvent()
}
