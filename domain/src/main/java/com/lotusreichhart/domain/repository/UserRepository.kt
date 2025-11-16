package com.lotusreichhart.domain.repository

import com.lotusreichhart.domain.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun fetchProfile(): Result<UserEntity>
    fun getProfileFlow(): Flow<Result<UserEntity>>
    suspend fun claimAdReward(): Result<UserEntity>
}