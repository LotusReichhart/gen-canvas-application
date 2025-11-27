package com.lotusreichhart.gencanvas.core.domain.repository

interface AuthRepository {
    suspend fun signInWithEmail(email: String, password: String): Result<Unit>
    suspend fun signInWithGoogle(idToken: String): Result<Unit>

    suspend fun requestSignUp(name: String, email: String, password: String): Result<Unit>
    suspend fun verifySignUp(otp: String, email: String): Result<Unit>

    suspend fun requestForgotPassword(email: String): Result<Unit>
    suspend fun verifyForgotPassword(otp: String, email: String): Result<String>
    suspend fun resetPassword(
        resetToken: String,
        newPassword: String,
        confirmPassword: String
    ): Result<Unit>

    suspend fun resendOtp(email: String): Result<Unit>

    suspend fun signOut(): Result<Unit>
}