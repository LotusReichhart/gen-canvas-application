package com.lotusreichhart.domain.usecase.user

import com.lotusreichhart.domain.entity.UserEntity
import com.lotusreichhart.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetProfileStreamUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<UserEntity?> {
        return userRepository.getProfileStream()
    }
}