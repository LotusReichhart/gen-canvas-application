package com.lotusreichhart.gencanvas.core.data.network.model.user

import com.google.gson.annotations.SerializedName
import kotlinx.datetime.Instant

data class UserCreditDto(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("balance")
    val balance: Int,
    @SerializedName("last_refill_processed_date")
    val lastRefillDate: Instant?
)
