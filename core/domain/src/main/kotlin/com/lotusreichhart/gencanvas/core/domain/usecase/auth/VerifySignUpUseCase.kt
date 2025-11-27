package com.lotusreichhart.gencanvas.core.domain.usecase.auth

import com.lotusreichhart.gencanvas.core.domain.repository.AuthRepository
import com.lotusreichhart.gencanvas.core.domain.repository.UserRepository
import com.lotusreichhart.gencanvas.core.model.user.User
import javax.inject.Inject

class VerifySignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(otp: String, email: String): Result<User> {
        val verifyResult = authRepository.verifySignUp(otp = otp, email = email)
        if (verifyResult.isFailure) {
            return Result.failure(verifyResult.exceptionOrNull()!!)
        }
        return userRepository.fetchProfile()
    }
}