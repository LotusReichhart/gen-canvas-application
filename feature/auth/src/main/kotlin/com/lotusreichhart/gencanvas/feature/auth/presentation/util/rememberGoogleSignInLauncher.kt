package com.lotusreichhart.gencanvas.feature.auth.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
internal fun rememberGoogleSignInLauncher(
    onSignInSuccess: (String) -> Unit,
    onSignInError: (String) -> Unit
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
                    onSignInError("Không tìm thấy ID Token")
                }
            } else {
                val error = result.exceptionOrNull()
                Timber.e(error,"rememberGoogleSignInLauncher error: ")
                onSignInError(error?.message ?: "Đăng nhập thất bại")
            }
        }
    }
}