package com.lotusreichhart.domain.use_cases.auth

import com.lotusreichhart.domain.repositories.AuthRepository

class SignOutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.signOut()
}