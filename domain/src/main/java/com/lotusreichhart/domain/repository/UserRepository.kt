package com.lotusreichhart.domain.repositories

import com.lotusreichhart.domain.entities.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun fetchProfile(): Result<UserEntity>
    fun getProfileFlow(): Flow<Result<UserEntity>>
    suspend fun claimAdReward(): Result<UserEntity>
}