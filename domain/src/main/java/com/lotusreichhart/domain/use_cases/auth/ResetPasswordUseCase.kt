package com.lotusreichhart.domain.use_cases.auth

import com.lotusreichhart.domain.repositories.AuthRepository

class ResetPasswordUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        resetToken: String,
        newPassword: String,
        confirmPassword: String
    ) = authRepository.resetPassword(resetToken, newPassword, confirmPassword)
}