package com.lotusreichhart.domain.use_case.auth

import com.lotusreichhart.domain.repository.AuthRepository

class SignOutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.signOut()
}