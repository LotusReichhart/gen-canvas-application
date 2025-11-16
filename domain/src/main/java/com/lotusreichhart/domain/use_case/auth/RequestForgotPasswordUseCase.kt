package com.lotusreichhart.domain.use_case.auth

import com.lotusreichhart.domain.repository.AuthRepository

class RequestForgotPasswordUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String) =
        authRepository.requestForgotPassword(email)
}