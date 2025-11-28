package com.lotusreichhart.gencanvas.core.model.banner

data class Banner(
    val id: Int? = null,
    val title: String? = null,
    val imageUrl: String,
    val actionUrl: String? = null,
    val displayOrder: Int
)
