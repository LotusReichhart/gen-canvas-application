package com.lotusreichhart.domain.use_cases.user

import com.lotusreichhart.domain.entities.UserEntity
import com.lotusreichhart.domain.repositories.UserRepository

class ClaimAdRewardUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Result<UserEntity> {
        return userRepository.claimAdReward()
    }
}