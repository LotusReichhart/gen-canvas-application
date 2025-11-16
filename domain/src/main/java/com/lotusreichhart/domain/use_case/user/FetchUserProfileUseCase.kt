package com.lotusreichhart.domain.use_case.user

import com.lotusreichhart.domain.entity.UserEntity
import com.lotusreichhart.domain.repository.UserRepository

class FetchUserProfileUseCase(
    private val userRepository: UserRepository

) {
    suspend operator fun invoke(): Result<UserEntity> {
        return userRepository.fetchProfile()
    }
}