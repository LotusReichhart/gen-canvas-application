package com.lotusreichhart.gencanvas.core.data.mapper

import com.lotusreichhart.gencanvas.core.data.database.model.user.UserCreditEntity
import com.lotusreichhart.gencanvas.core.data.database.model.user.UserEntity
import com.lotusreichhart.gencanvas.core.data.database.model.user.UserWithCreditEntity
import com.lotusreichhart.gencanvas.core.data.network.model.user.UserProfileResponse
import com.lotusreichhart.gencanvas.core.model.user.User
import com.lotusreichhart.gencanvas.core.model.user.enums.AuthProvider
import com.lotusreichhart.gencanvas.core.model.user.enums.UserStatus
import com.lotusreichhart.gencanvas.core.model.user.enums.UserTier

private fun String.toUserStatus() =
    UserStatus.entries.firstOrNull { it.name.equals(this, ignoreCase = true) }
        ?: UserStatus.ACTIVE

private fun String.toUserTier() =
    UserTier.entries.firstOrNull { it.name.equals(this, ignoreCase = true) }
        ?: UserTier.FREE

private fun String.toAuthProvider() =
    AuthProvider.entries.firstOrNull { it.name.equals(this, ignoreCase = true) }
        ?: AuthProvider.EMAIL


fun UserProfileResponse.toUserEntity(): UserEntity {
    return UserEntity(
        id = this.user.id,
        name = this.user.name,
        email = this.user.email,
        avatarUrl = this.user.avatarUrl,
        status = this.user.status,
        tier = this.user.tier,
        authProvider = this.user.authProvider
    )
}

fun UserProfileResponse.toUserCreditEntity(): UserCreditEntity {
    return UserCreditEntity(
        userId = this.credit.userId,
        balance = this.credit.balance,
        lastRefillDate = this.credit.lastRefillDate
    )
}

fun UserWithCreditEntity.toModel(): User {
    return User(
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

fun UserProfileResponse.toModel(): User {
    return User(
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