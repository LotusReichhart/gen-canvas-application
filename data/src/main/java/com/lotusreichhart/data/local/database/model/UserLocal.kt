package com.lotusreichhart.data.local.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserLocal(
    @PrimaryKey
    val id: Int,
    val name: String,
    val email: String,
    val avatarUrl: String? = null,
    val status: String,
    val tier: String,
    val authProvider: String
)
