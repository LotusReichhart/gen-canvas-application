package com.lotusreichhart.gencanvas.core.common

fun String?.toInitials(): String {
    if (this.isNullOrBlank()) return ""

    val trimmed = this.trim()
    val parts = trimmed.split(Regex("\\s+"))

    val firstInitial = parts.getOrNull(0)?.firstOrNull()?.uppercase() ?: ""

    val secondInitial = parts.getOrNull(1)?.firstOrNull()?.uppercase() ?: ""

    return if (firstInitial.isNotEmpty()) {
        "$firstInitial$secondInitial"
    } else {
        ""
    }
}