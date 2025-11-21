package com.lotusreichhart.domain.usecase.auth

import com.lotusreichhart.domain.repository.AuthRepository

class VerifyForgotPasswordUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(otp: String, email: String) =
        authRepository.verifyForgotPassword(otp, email)
}