package com.lotusreichhart.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lotusreichhart.data.local.database.model.LegalLocal

@Dao
interface LegalDao {
    @Query("SELECT * FROM legal_info WHERE id = 0")
    suspend fun getLegalInfo(): LegalLocal?

    @Upsert
    suspend fun saveLegalInfo(legalLocal: LegalLocal)
}