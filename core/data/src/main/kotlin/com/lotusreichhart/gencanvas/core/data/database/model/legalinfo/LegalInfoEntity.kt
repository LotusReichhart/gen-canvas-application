package com.lotusreichhart.gencanvas.core.data.database.model.legalinfo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "legal_info")
data class LegalInfoEntity(
    @PrimaryKey
    val id: Int = 0,
    val termsUrl: String,
    val termsVersion: String,
    val privacyUrl: String,
    val privacyVersion: String
)
