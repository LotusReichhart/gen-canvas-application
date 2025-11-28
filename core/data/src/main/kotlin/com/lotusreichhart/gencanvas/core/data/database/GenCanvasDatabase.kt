package com.lotusreichhart.gencanvas.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lotusreichhart.gencanvas.core.data.database.dao.BannerDao
import com.lotusreichhart.gencanvas.core.data.database.dao.LegalInfoDao
import com.lotusreichhart.gencanvas.core.data.database.dao.UserDao
import com.lotusreichhart.gencanvas.core.data.database.model.banner.BannerEntity
import com.lotusreichhart.gencanvas.core.data.database.model.legalinfo.LegalInfoEntity
import com.lotusreichhart.gencanvas.core.data.database.model.user.UserCreditEntity
import com.lotusreichhart.gencanvas.core.data.database.model.user.UserEntity
import com.lotusreichhart.gencanvas.core.data.database.util.InstantConverter

@Database(
    entities = [
        UserEntity::class,
        UserCreditEntity::class,
        LegalInfoEntity::class,
        BannerEntity::class
    ],
    version = 1,
    exportSchema = true,
    autoMigrations = []
)

@TypeConverters(
    InstantConverter::class,
)
internal abstract class GenCanvasDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun legalInfoDao(): LegalInfoDao
    abstract fun bannerDao(): BannerDao
}