package com.lotusreichhart.domain.use_cases.auth

import com.lotusreichhart.domain.entities.UserEntity
import com.lotusreichhart.domain.repositories.AuthRepository
import com.lotusreichhart.domain.repositories.UserRepository

class SignInWithGoogleUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(idToken: String): Result<UserEntity> {
        val loginResult = authRepository.signInWithGoogle(idToken)
        if (loginResult.isFailure) {
            return Result.failure(loginResult.exceptionOrNull()!!)
        }
        return userRepository.fetchProfile()
    }
}