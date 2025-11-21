package com.lotusreichhart.data.local.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_credit",
    foreignKeys = [
        ForeignKey(
            entity = UserLocal::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CreditLocal(
    @PrimaryKey
    val userId: Int,
    val balance: Int,
    val lastRefillDate: String?
)
