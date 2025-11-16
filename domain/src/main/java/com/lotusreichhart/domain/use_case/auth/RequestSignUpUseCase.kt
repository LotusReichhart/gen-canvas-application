package com.lotusreichhart.domain.use_case.auth

import com.lotusreichhart.domain.repository.AuthRepository

class RequestSignUpUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String) =
        authRepository.requestSignUp(name, email, password)
}