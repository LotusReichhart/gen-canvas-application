package com.lotusreichhart.gencanvas.core.model

import java.util.UUID

data class Project(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val createdAt: Long,
    val updatedAt: Long,
    val thumbnailUri: String?,
    val currentImageUri: String?,
    val isSynced: Boolean = false
)
