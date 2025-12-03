package com.lotusreichhart.gencanvas.feature.auth.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.lotusreichhart.gencanvas.core.common.util.TextResource
import com.lotusreichhart.gencanvas.core.data.network.util.ServerException
import kotlinx.coroutines.launch
import timber.log.Timber

import com.lotusreichhart.gencanvas.feature.auth.R
import com.lotusreichhart.gencanvas.core.common.R as CoreR

@Composable
internal fun rememberGoogleSignInLauncher(
    onSignInSuccess: (String) -> Unit,
    onSignInError: (TextResource) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val googleSignInHelper = remember { GoogleSignInHelper(context) }

    return {
        scope.launch {
            val result = googleSignInHelper.signIn()
            Timber.d("rememberGoogleSignInLauncher result $result")


            if (result.isSuccess) {
                val idToken = result.getOrNull()
                if (idToken != null) {
                    onSignInSuccess(idToken)
                } else {
                    onSignInError(TextResource.Id(R.string.error_google_sign_in_failed))
                }
            } else {
                val error = result.exceptionOrNull()
                Timber.e(error,"rememberGoogleSignInLauncher error: ")
                val errorText = if (error is ServerException) {
                    error.textResource
                } else {
                    TextResource.Id(CoreR.string.core_unknow_error)
                }

                onSignInError(errorText)
            }
        }
    }
}