package com.lotusreichhart.gencanvas.core.data.database.model.user

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithCreditEntity(
    @Embedded
    val user: UserEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val credit: UserCreditEntity?
)
