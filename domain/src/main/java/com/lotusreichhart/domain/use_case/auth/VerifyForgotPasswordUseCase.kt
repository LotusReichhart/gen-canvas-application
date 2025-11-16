package com.lotusreichhart.domain.use_cases.auth

import com.lotusreichhart.domain.repositories.AuthRepository

class VerifyForgotPasswordUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(otp: String, email: String) =
        authRepository.verifyForgotPassword(otp, email)
}