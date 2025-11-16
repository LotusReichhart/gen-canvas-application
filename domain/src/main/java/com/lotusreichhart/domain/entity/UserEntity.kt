package com.lotusreichhart.domain.entity

enum class UserStatus {
    ACTIVE,
    INACTIVE,
    BANNED
}

enum class UserTier {
    FREE,
    PRO
}

enum class AuthProvider {
    EMAIL,
    GOOGLE
}

data class UserEntity(
    val id: Int? = null,
    val name: String,
    val email: String,
    val avatar: String?,
    val status: UserStatus,
    val tier: UserTier,
    val authProvider: AuthProvider
)