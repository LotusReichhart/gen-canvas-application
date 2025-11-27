package com.lotusreichhart.gencanvas.core.domain.usecase.auth

import com.lotusreichhart.gencanvas.core.domain.repository.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        resetToken: String,
        newPassword: String,
        confirmPassword: String
    ) = authRepository.resetPassword(resetToken, newPassword, confirmPassword)
}