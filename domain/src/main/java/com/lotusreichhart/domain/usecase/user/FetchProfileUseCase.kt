package com.lotusreichhart.domain.usecase.user

import com.lotusreichhart.domain.entity.UserEntity
import com.lotusreichhart.domain.repository.UserRepository

class FetchProfileUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<UserEntity> {
        return userRepository.fetchProfile()
    }
}