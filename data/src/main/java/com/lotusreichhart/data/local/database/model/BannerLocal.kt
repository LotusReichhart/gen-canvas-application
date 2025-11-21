package com.lotusreichhart.data.local.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "banners")
data class BannerLocal(
    @PrimaryKey
    val id: Int,
    val title: String?,
    val imageUrl: String,
    val actionUrl: String?,
    val displayOrder: Int
)