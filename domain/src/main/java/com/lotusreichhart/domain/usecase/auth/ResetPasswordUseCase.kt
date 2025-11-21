package com.lotusreichhart.domain.usecase.auth

import com.lotusreichhart.domain.repository.AuthRepository

class ResetPasswordUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        resetToken: String,
        newPassword: String,
        confirmPassword: String
    ) = authRepository.resetPassword(resetToken, newPassword, confirmPassword)
}