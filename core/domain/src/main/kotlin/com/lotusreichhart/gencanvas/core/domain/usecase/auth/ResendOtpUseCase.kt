package com.lotusreichhart.gencanvas.core.domain.usecase.auth

import com.lotusreichhart.gencanvas.core.domain.repository.AuthRepository
import javax.inject.Inject

class ResendOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String) =
        authRepository.resendOtp(email)
}