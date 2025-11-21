package com.lotusreichhart.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lotusreichhart.data.local.database.dao.BannerDao
import com.lotusreichhart.data.local.database.dao.LegalDao
import com.lotusreichhart.data.local.database.dao.UserDao
import com.lotusreichhart.data.local.database.model.BannerLocal
import com.lotusreichhart.data.local.database.model.CreditLocal
import com.lotusreichhart.data.local.database.model.LegalLocal
import com.lotusreichhart.data.local.database.model.UserLocal

@Database(
    entities = [
        BannerLocal::class,
        UserLocal::class,
        CreditLocal::class,
        LegalLocal::class
    ],
    version = 2,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun bannerDao(): BannerDao
    abstract fun userDao(): UserDao
    abstract fun legalDao(): LegalDao
}