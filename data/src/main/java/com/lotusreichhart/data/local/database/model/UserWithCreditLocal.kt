package com.lotusreichhart.data.local.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithCreditLocal(
    @Embedded
    val user: UserLocal,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val credit: CreditLocal?
)
