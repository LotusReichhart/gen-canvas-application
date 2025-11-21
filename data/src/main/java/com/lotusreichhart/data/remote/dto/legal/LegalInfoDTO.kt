package com.lotusreichhart.data.remote.dto.legal

import com.google.gson.annotations.SerializedName

data class LegalInfoDTO(
    @SerializedName("terms_of_service")
    val termsOfService: LegalDocDTO,

    @SerializedName("privacy_policy")
    val privacyPolicy: LegalDocDTO
)


