package com.lotusreichhart.gencanvas.core.domain.usecase.auth

import com.lotusreichhart.gencanvas.core.domain.repository.AuthRepository
import com.lotusreichhart.gencanvas.core.domain.repository.UserRepository
import com.lotusreichhart.gencanvas.core.model.user.User
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(idToken: String): Result<User> {
        val loginResult = authRepository.signInWithGoogle(idToken)
        if (loginResult.isFailure) {
            return Result.failure(loginResult.exceptionOrNull()!!)
        }
        return userRepository.fetchProfile()
    }
}