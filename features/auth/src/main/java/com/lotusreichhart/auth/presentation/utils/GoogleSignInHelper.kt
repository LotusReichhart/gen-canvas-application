package com.lotusreichhart.auth.presentation.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.lotusreichhart.auth.BuildConfig
import com.lotusreichhart.core.utils.logD
import com.lotusreichhart.core.utils.logE

class GoogleSignInHelper(
    private val context: Context
) {
    suspend fun signIn(): Result<String> {
        return try {
            hideKeyboard()

            val credentialManager = CredentialManager.create(context)

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            logD("signIn $result")

            val credential = result.credential
            logD("credential type: ${credential.javaClass.name}")

            when (credential) {
                is GoogleIdTokenCredential -> {
                    logD("credential is GoogleIdTokenCredential")
                    val idToken = credential.idToken
                    Result.success(idToken)
                }

                is CustomCredential -> {
                    logD("credential is CustomCredential: ${credential.type}")

                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        try {
                            logD("credential data: ${credential.data}")

                            val googleIdTokenCredential =
                                GoogleIdTokenCredential.createFrom(credential.data)

                            val idToken = googleIdTokenCredential.idToken
                            Result.success(idToken)
                        } catch (e: Exception) {
                            logE("Failed to parse GoogleIdTokenCredential", e)
                            Result.failure(Exception("Đã xảy ra lỗi khi đăng nhập bằng tài khoản Google"))
                        }
                    } else {
                        logE("Unknown CustomCredential type: ${credential.type}")
                        Result.failure(Exception("Đã xảy ra lỗi khi đăng nhập bằng tài khoản Google"))
                    }
                }

                else -> {
                    logE("Unknown credential type: ${credential.javaClass.name}")
                    Result.failure(Exception("Đã xảy ra lỗi khi đăng nhập bằng tài khoản Google"))
                }
            }
        } catch (e: GetCredentialException) {
            logE("signIn GetCredentialException:  ", e)
            Result.failure(Exception("Đã xảy ra lỗi khi đăng nhập bằng tài khoản Google"))
        } catch (e: Exception) {
            logE("signIn Exception: ", e)
            Result.failure(Exception("Đã xảy ra lỗi khi đăng nhập bằng tài khoản Google"))
        }
    }

    private fun hideKeyboard() {
        val activity = context as? Activity ?: return
        val view = activity.currentFocus ?: View(activity)
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}