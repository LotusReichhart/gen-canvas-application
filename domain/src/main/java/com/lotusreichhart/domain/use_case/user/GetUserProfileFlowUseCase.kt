package com.lotusreichhart.domain.use_case.user

import com.lotusreichhart.domain.entity.UserEntity
import com.lotusreichhart.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserProfileFlowUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Result<UserEntity>> {
        return userRepository.getProfileFlow()
    }
}