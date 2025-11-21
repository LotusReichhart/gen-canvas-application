package com.lotusreichhart.data.local.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "legal_info")
data class LegalLocal(
    @PrimaryKey
    val id: Int = 0,
    val termsUrl: String,
    val termsVersion: String,
    val privacyUrl: String,
    val privacyVersion: String
)
