package com.lotusreichhart.domain.use_case.auth

import com.lotusreichhart.domain.entity.UserEntity
import com.lotusreichhart.domain.repository.AuthRepository
import com.lotusreichhart.domain.repository.UserRepository

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