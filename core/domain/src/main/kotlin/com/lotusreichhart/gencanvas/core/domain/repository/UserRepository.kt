package com.lotusreichhart.gencanvas.core.domain.repository

import com.lotusreichhart.gencanvas.core.model.user.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getProfileStream(): Flow<User?>
    suspend fun fetchProfile(): Result<User>
    suspend fun clearUserProfile()
}