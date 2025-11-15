package com.lotusreichhart.core.utils

sealed class UiEvent {
    data class ShowToast(val message: String) : UiEvent()
    data class ShowSnackBar(val message: String, val actionLabel: String? = null) : UiEvent()
    data class ShowDialog(
        val title: String,
        val message: String,
        val positiveButtonText: String = "OK",
        val onConfirm: () -> Unit = {}
    ) : UiEvent()
}