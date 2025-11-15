package com.lotusreichhart.domain.use_cases.auth

import com.lotusreichhart.domain.repositories.AuthRepository

class ResendOtpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String) =
        authRepository.resendOtp(email)
}