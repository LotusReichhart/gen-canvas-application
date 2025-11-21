package com.lotusreichhart.data.mapper

import com.lotusreichhart.data.local.database.model.CreditLocal
import com.lotusreichhart.data.local.database.model.UserLocal
import com.lotusreichhart.data.local.database.model.UserWithCreditLocal
import com.lotusreichhart.data.remote.dto.user.UserProfileResponse
import com.lotusreichhart.domain.entity.AuthProvider
import com.lotusreichhart.domain.entity.UserEntity
import com.lotusreichhart.domain.entity.UserStatus
import com.lotusreichhart.domain.entity.UserTier

private fun String.toUserStatus() =
    UserStatus.entries.firstOrNull { it.name.equals(this, ignoreCase = true) }
        ?: UserStatus.ACTIVE

private fun String.toUserTier() =
    UserTier.entries.firstOrNull { it.name.equals(this, ignoreCase = true) }
        ?: UserTier.FREE

private fun String.toAuthProvider() =
    AuthProvider.entries.firstOrNull { it.name.equals(this, ignoreCase = true) }
        ?: AuthProvider.EMAIL


fun UserProfileResponse.toUserLocal(): UserLocal {
    return UserLocal(
        id = this.user.id,
        name = this.user.name,
        email = this.user.email,
        avatarUrl = this.user.avatarUrl,
        status = this.user.status,
        tier = this.user.tier,
        authProvider = this.user.authProvider
    )
}

fun UserProfileResponse.toCreditLocal(): CreditLocal {
    return CreditLocal(
        userId = this.credit.userId,
        balance = this.credit.balance,
        lastRefillDate = this.credit.lastRefillDate
    )
}

fun UserWithCreditLocal.toDomain(): UserEntity {
    return UserEntity(
        id = this.user.id,
        name = this.user.name,
        email = this.user.email,
        avatarUrl = this.user.avatarUrl,
        status = this.user.status.toUserStatus(),
        tier = this.user.tier.toUserTier(),
        authProvider = this.user.authProvider.toAuthProvider(),

        balance = this.credit?.balance ?: 0,
        lastRefillDate = this.credit?.lastRefillDate
    )
}

fun UserProfileResponse.toDomain(): UserEntity {
    return UserEntity(
        id = this.user.id,
        name = this.user.name,
        email = this.user.email,
        avatarUrl = this.user.avatarUrl,

        status = this.user.status.toUserStatus(),
        tier = this.user.tier.toUserTier(),
        authProvider = this.user.authProvider.toAuthProvider(),

        balance = this.credit.balance,
        lastRefillDate = this.credit.lastRefillDate
    )
}
