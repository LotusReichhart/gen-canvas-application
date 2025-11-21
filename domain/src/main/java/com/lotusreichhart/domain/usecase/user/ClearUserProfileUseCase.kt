package com.lotusreichhart.domain.usecase.user

import com.lotusreichhart.domain.repository.UserRepository

class ClearUserProfileUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() {
        userRepository.clearUserProfile()
    }
}