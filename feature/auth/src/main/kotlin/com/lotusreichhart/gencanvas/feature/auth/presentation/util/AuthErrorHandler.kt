package com.lotusreichhart.gencanvas.feature.auth.presentation.util

import com.lotusreichhart.gencanvas.core.common.R
import com.lotusreichhart.gencanvas.core.common.event.GlobalUiEventManager
import com.lotusreichhart.gencanvas.core.common.event.UiEvent
import com.lotusreichhart.gencanvas.core.common.util.TextResource
import com.lotusreichhart.gencanvas.core.data.network.util.ServerException
import timber.log.Timber
import javax.inject.Inject

internal class AuthErrorHandler @Inject constructor(
    private val globalUiEventManager: GlobalUiEventManager
) {
    suspend fun handleError(
        exception: Throwable?,
        onFieldErrors: (Map<String, String>) -> Unit
    ) {
        Timber.e(exception,"handleError exception")

        if (exception is ServerException && exception.fieldErrors != null) {
            val errors = exception.fieldErrors!!

            Timber.d("errors body: $errors")

            onFieldErrors(errors)

            val generalMessage = errors["general"] ?: errors["message"]

            if (generalMessage != null) {
                globalUiEventManager.showSnackBar(
                    message = TextResource.Raw(generalMessage),
                    type = UiEvent.SnackBarType.ERROR
                )
            } else {
                val isFieldSpecificError = errors.keys.any {
                    it in listOf("email", "password", "name", "otp", "confirm_password", "new_password")
                }

                if (!isFieldSpecificError) {
                    globalUiEventManager.showSnackBar(
                        message = exception.textResource,
                        type = UiEvent.SnackBarType.ERROR
                    )
                }
            }
        } else {
            val message: TextResource = if (exception is ServerException) {
                exception.textResource
            } else {
                exception?.message?.let { TextResource.Raw(it) }
                    ?: TextResource.Id(R.string.core_unknow_error)
            }
            globalUiEventManager.showSnackBar(message, type = UiEvent.SnackBarType.ERROR)
        }
    }
}