package com.lotusreichhart.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lotusreichhart.data.local.database.model.BannerLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface BannerDao {
    @Query("SELECT * FROM banners ORDER BY displayOrder ASC")
    fun getBannersFlow(): Flow<List<BannerLocal>>

    @Upsert
    suspend fun saveBanners(banners: List<BannerLocal>)

    @Query("DELETE FROM banners")
    suspend fun clearBanners()
}