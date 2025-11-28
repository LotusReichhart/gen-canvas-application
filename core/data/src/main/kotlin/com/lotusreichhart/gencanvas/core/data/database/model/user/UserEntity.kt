package com.lotusreichhart.gencanvas.core.data.database.model.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val email: String,
    val avatarUrl: String? = null,
    val status: String,
    val tier: String,
    val authProvider: String
)
