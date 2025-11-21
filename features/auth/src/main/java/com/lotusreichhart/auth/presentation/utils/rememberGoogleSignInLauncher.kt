package com.lotusreichhart.auth.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.lotusreichhart.core.utils.logD
import com.lotusreichhart.core.utils.logE
import kotlinx.coroutines.launch

@Composable
fun rememberGoogleSignInLauncher(
    onSignInSuccess: (String) -> Unit,
    onSignInError: (String) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val googleSignInHelper = remember { GoogleSignInHelper(context) }

    return {
        scope.launch {
            val result = googleSignInHelper.signIn()
            logD("rememberGoogleSignInLauncher result $result")

            if (result.isSuccess) {
                val idToken = result.getOrNull()
                if (idToken != null) {
                    onSignInSuccess(idToken)
                } else {
                    onSignInError("Không tìm thấy ID Token")
                }
            } else {
                val error = result.exceptionOrNull()
                logE("rememberGoogleSignInLauncher error: ", error)
                onSignInError(error?.message ?: "Đăng nhập thất bại")
            }
        }
    }
}