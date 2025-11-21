package com.lotusreichhart.domain.repository

import com.lotusreichhart.domain.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getProfileStream(): Flow<UserEntity?>
    suspend fun fetchProfile(): Result<UserEntity>

    suspend fun clearUserProfile()
}