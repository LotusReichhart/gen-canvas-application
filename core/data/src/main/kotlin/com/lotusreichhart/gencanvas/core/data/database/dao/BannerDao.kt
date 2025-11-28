package com.lotusreichhart.gencanvas.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.lotusreichhart.gencanvas.core.data.database.model.banner.BannerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BannerDao {
    @Query("SELECT * FROM banners ORDER BY displayOrder ASC")
    fun getBannersFlow(): Flow<List<BannerEntity>>

    @Query("SELECT * FROM banners ORDER BY displayOrder ASC")
    suspend fun getBanners(): List<BannerEntity>

    @Upsert
    suspend fun saveBanners(banners: List<BannerEntity>)

    @Query("DELETE FROM banners")
    suspend fun clearBanners()

    @Transaction
    suspend fun replaceBanners(banners: List<BannerEntity>) {
        clearBanners()
        saveBanners(banners)
    }
}