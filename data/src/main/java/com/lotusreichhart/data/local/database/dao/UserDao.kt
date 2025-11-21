package com.lotusreichhart.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.lotusreichhart.data.local.database.model.CreditLocal
import com.lotusreichhart.data.local.database.model.UserLocal
import com.lotusreichhart.data.local.database.model.UserWithCreditLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Transaction
    @Query("SELECT * FROM user_profile LIMIT 1")
    fun getUserWithCreditFlow(): Flow<UserWithCreditLocal?>

    @Upsert
    suspend fun saveUser(user: UserLocal)

    @Upsert
    suspend fun saveCredit(credit: CreditLocal)

    @Query("DELETE FROM user_profile")
    suspend fun deleteUser()
}