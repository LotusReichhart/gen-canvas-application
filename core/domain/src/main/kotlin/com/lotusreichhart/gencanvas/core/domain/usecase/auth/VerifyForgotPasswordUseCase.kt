package com.lotusreichhart.gencanvas.core.domain.usecase.auth

import com.lotusreichhart.gencanvas.core.domain.repository.AuthRepository
import javax.inject.Inject

class VerifyForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(otp: String, email: String) =
        authRepository.verifyForgotPassword(otp, email)
}