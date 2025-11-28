package com.lotusreichhart.gencanvas.core.model.user

import com.lotusreichhart.gencanvas.core.model.user.enums.AuthProvider
import com.lotusreichhart.gencanvas.core.model.user.enums.UserStatus
import com.lotusreichhart.gencanvas.core.model.user.enums.UserTier
import kotlinx.datetime.Instant

data class User(
    val id: Int? = null,
    val name: String,
    val email: String,
    val avatarUrl: String?,
    val status: UserStatus,
    val tier: UserTier,
    val authProvider: AuthProvider,
    val balance: Int,
    val lastRefillDate: Instant?
)