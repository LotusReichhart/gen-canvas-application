package com.lotusreichhart.gencanvas.core.domain.usecase.auth

import com.lotusreichhart.gencanvas.core.domain.repository.AuthRepository
import javax.inject.Inject

class RequestSignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, password: String) =
        authRepository.requestSignUp(name, email, password)
}