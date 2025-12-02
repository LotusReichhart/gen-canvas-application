package com.lotusreichhart.gencanvas.feature.auth.presentation.util

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
import com.lotusreichhart.gencanvas.core.common.util.TextResource
import com.lotusreichhart.gencanvas.core.data.network.util.ServerException
import com.lotusreichhart.gencanvas.feature.auth.BuildConfig
import com.lotusreichhart.gencanvas.feature.auth.R
import timber.log.Timber

internal class GoogleSignInHelper(
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

            Timber.d("signIn $result")

            val credential = result.credential
            Timber.d("credential type: ${credential.javaClass.name}")


            when (credential) {
                is GoogleIdTokenCredential -> {
                    Timber.d("credential data: ${credential.data}")

                    val idToken = credential.idToken
                    Result.success(idToken)
                }

                is CustomCredential -> {
                    Timber.d("credential is CustomCredential: ${credential.type}")


                    if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        try {
                            Timber.d("credential data: ${credential.data}")


                            val googleIdTokenCredential =
                                GoogleIdTokenCredential.createFrom(credential.data)

                            val idToken = googleIdTokenCredential.idToken
                            Result.success(idToken)
                        } catch (e: Exception) {
                            Timber.e(e,"Failed to parse GoogleIdTokenCredential")
                            Result.failure(getGoogleSignInError())
                        }
                    } else {
                        Timber.e("Unknown CustomCredential type: ${credential.type}")
                        Result.failure(getGoogleSignInError())
                    }
                }

                else -> {
                    Timber.e("Unknown credential type: ${credential.javaClass.name}")
                    Result.failure(getGoogleSignInError())
                }
            }
        } catch (e: GetCredentialException) {
            Timber.e(e,"signIn GetCredentialException:  ")
            Result.failure(getGoogleSignInError())
        } catch (e: Exception) {
            Timber.e(e,"signIn Exception:  ")
            Result.failure(getGoogleSignInError())
        }
    }

    private fun getGoogleSignInError(): Exception {
        return ServerException(
            textResource = TextResource.Id(R.string.error_google_sign_in_failed)
        )
    }

    private fun hideKeyboard() {
        val activity = context as? Activity ?: return
        val view = activity.currentFocus ?: View(activity)
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}