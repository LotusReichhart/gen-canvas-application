package com.lotusreichhart.gencanvas.core.data.repository

import com.lotusreichhart.gencanvas.core.data.database.dao.UserDao
import com.lotusreichhart.gencanvas.core.data.mapper.toModel
import com.lotusreichhart.gencanvas.core.data.mapper.toUserCreditEntity
import com.lotusreichhart.gencanvas.core.data.mapper.toUserEntity
import com.lotusreichhart.gencanvas.core.data.network.service.UserApiService
import com.lotusreichhart.gencanvas.core.data.network.util.safeApiCallData
import com.lotusreichhart.gencanvas.core.domain.repository.UserRepository
import com.lotusreichhart.gencanvas.core.model.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: UserApiService,
    private val dao: UserDao
) : UserRepository {

    override fun getProfileStream(): Flow<User?> {
        return dao.getUserWithCreditFlow()
            .map { userWithUserCreditEntity ->
                userWithUserCreditEntity?.toModel()
            }
    }

    override suspend fun fetchProfile(): Result<User> {
        val result = safeApiCallData {
            apiService.getProfile()
        }

        result.onSuccess { userProfileResponse ->
            try {
                val newUserEntity = userProfileResponse.toUserEntity()
                val newUserCreditEntity = userProfileResponse.toUserCreditEntity()

                val oldUserEntity = dao.getUserById(newUserEntity.id)
                if (oldUserEntity == null || oldUserEntity != newUserEntity) {
                    Timber.d("User data thay đổi -> Cập nhật RoomDB")
                    dao.saveUser(newUserEntity)
                } else {
                    Timber.d("User data không đổi -> Bỏ qua ghi RoomDB")
                }

                val oldUserCreditEntity = dao.getCreditByUserId(newUserCreditEntity.userId)
                if (oldUserCreditEntity == null || oldUserCreditEntity != newUserCreditEntity) {
                    Timber.d("Credit data thay đổi -> Cập nhật RoomDB")
                    dao.saveCredit(newUserCreditEntity)
                } else {
                    Timber.d("Credit data không đổi -> Bỏ qua ghi RoomDB")
                }
            } catch (e: Exception) {
                Timber.e(e, "Lưu RoomDB thất bại")
            }
        }

        return result.map { it.toModel() }
    }

    override suspend fun clearUserProfile() {
        dao.deleteUser()
    }
}