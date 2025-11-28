package com.lotusreichhart.gencanvas.core.data.database.model.user

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "user_credit",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserCreditEntity(
    @PrimaryKey
    val userId: Int,
    val balance: Int,
    val lastRefillDate: Instant?
)
