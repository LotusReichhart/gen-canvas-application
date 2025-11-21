package com.lotusreichhart.data.repository

import com.lotusreichhart.core.utils.logE
import com.lotusreichhart.data.local.database.dao.UserDao
import com.lotusreichhart.data.mapper.toCreditLocal
import com.lotusreichhart.data.mapper.toDomain
import com.lotusreichhart.data.mapper.toUserLocal
import com.lotusreichhart.data.remote.service.UserApiService
import com.lotusreichhart.data.utils.safeApiCallData
import com.lotusreichhart.domain.entity.UserEntity
import com.lotusreichhart.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService,
    private val userDao: UserDao
) : UserRepository {

    override fun getProfileStream(): Flow<UserEntity?> {
        return userDao.getUserWithCreditFlow()
            .map { userWithCreditLocal ->
                userWithCreditLocal?.toDomain()
            }
    }

    override suspend fun fetchProfile(): Result<UserEntity> {
        val result = safeApiCallData {
            userApiService.getProfile()
        }

        result.onSuccess { userProfileResponse ->
            try {
                userDao.saveUser(userProfileResponse.toUserLocal())
                userDao.saveCredit(userProfileResponse.toCreditLocal())
            } catch (e: Exception) {
                logE("Lưu RoomDB thất bại", e)
                return Result.failure(e)
            }
        }

        return result.map { it.toDomain() }

    }

    override suspend fun clearUserProfile() {
        userDao.deleteUser()
    }
}