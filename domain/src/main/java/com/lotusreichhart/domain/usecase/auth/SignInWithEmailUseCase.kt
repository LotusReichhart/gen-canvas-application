package com.lotusreichhart.domain.usecase.auth

import com.lotusreichhart.domain.entity.UserEntity
import com.lotusreichhart.domain.repository.AuthRepository
import com.lotusreichhart.domain.repository.UserRepository

class SignInWithEmailUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<UserEntity> {
        val loginResult = authRepository.signInWithEmail(email, password)
        if (loginResult.isFailure) {
            return Result.failure(loginResult.exceptionOrNull()!!)
        }
        return userRepository.fetchProfile()
    }
}