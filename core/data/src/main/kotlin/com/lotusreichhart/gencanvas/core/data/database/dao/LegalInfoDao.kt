package com.lotusreichhart.gencanvas.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lotusreichhart.gencanvas.core.data.database.model.legalinfo.LegalInfoEntity

@Dao
interface LegalInfoDao {
    @Query("SELECT * FROM legal_info WHERE id = 0")
    suspend fun getLegalInfo(): LegalInfoEntity?

    @Upsert
    suspend fun saveLegalInfo(legalLocal: LegalInfoEntity)
}