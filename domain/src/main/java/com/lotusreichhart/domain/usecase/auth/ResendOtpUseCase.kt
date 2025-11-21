package com.lotusreichhart.domain.usecase.auth

import com.lotusreichhart.domain.repository.AuthRepository

class ResendOtpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String) =
        authRepository.resendOtp(email)
}