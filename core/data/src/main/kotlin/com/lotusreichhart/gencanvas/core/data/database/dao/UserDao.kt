package com.lotusreichhart.gencanvas.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.lotusreichhart.gencanvas.core.data.database.model.user.UserCreditEntity
import com.lotusreichhart.gencanvas.core.data.database.model.user.UserEntity
import com.lotusreichhart.gencanvas.core.data.database.model.user.UserWithCreditEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Transaction
    @Query("SELECT * FROM user LIMIT 1")
    fun getUserWithCreditFlow(): Flow<UserWithCreditEntity?>

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity?

    @Query("SELECT * FROM user_credit WHERE userId = :userId")
    suspend fun getCreditByUserId(userId: Int): UserCreditEntity?

    @Upsert
    suspend fun saveUser(user: UserEntity)

    @Upsert
    suspend fun saveCredit(credit: UserCreditEntity)

    @Query("DELETE FROM user")
    suspend fun deleteUser()
}