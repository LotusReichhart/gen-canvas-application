package com.lotusreichhart.domain.use_cases.user

import com.lotusreichhart.domain.entities.UserEntity
import com.lotusreichhart.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserProfileFlowUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Result<UserEntity>> {
        return userRepository.getProfileFlow()
    }
}