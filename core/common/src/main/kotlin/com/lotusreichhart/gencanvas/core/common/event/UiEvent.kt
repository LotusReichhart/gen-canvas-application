package com.lotusreichhart.gencanvas.core.common.event

import com.lotusreichhart.gencanvas.core.common.util.TextResource

sealed class UiEvent {

    enum class SnackBarType { INFO, SUCCESS, WARNING, ERROR }

    data class ShowToast(val message: TextResource) : UiEvent()

    data class ShowSnackBar(
        val message: TextResource,
        val actionLabel: TextResource? = null,
        val type: SnackBarType = SnackBarType.INFO
    ) : UiEvent()

    data class ShowDialog(
        val title: TextResource,
        val message: TextResource,

        val positiveButtonText: TextResource = TextResource.Raw("OK"),
        val onPositiveClick: () -> Unit = {},

        val negativeButtonText: TextResource? = null,
        val onNegativeClick: (() -> Unit)? = null
    ) : UiEvent()

    data class Navigate(val route: String) : UiEvent()
}