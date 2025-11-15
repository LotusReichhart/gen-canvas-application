package com.lotusreichhart.domain.use_cases.auth

import com.lotusreichhart.domain.entities.UserEntity
import com.lotusreichhart.domain.repositories.AuthRepository
import com.lotusreichhart.domain.repositories.UserRepository

class VerifySignUpUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(otp: String, email: String): Result<UserEntity> {
        val verifyResult = authRepository.verifySignUp(otp, email)
        if (verifyResult.isFailure) {
            return Result.failure(verifyResult.exceptionOrNull()!!)
        }
        return userRepository.fetchProfile()
    }
}