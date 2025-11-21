package com.lotusreichhart.domain.usecase.auth

import com.lotusreichhart.domain.entity.UserEntity
import com.lotusreichhart.domain.repository.AuthRepository
import com.lotusreichhart.domain.repository.UserRepository

class VerifySignUpUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(otp: String, email: String): Result<UserEntity> {
        val verifyResult = authRepository.verifySignUp(otp = otp, email = email)
        if (verifyResult.isFailure) {
            return Result.failure(verifyResult.exceptionOrNull()!!)
        }
        return userRepository.fetchProfile()
    }
}