package com.lotusreichhart.gencanvas.core.domain.repository

import com.lotusreichhart.gencanvas.core.model.user.User
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository {
    fun getProfileStream(): Flow<User?>
    suspend fun fetchProfile(): Result<User>

    suspend fun updateUserProfile(name: String?, avatarFile: File?): Result<User>
    suspend fun clearUserProfile()
}