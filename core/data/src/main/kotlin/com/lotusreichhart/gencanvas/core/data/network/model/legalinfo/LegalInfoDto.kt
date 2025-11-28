package com.lotusreichhart.gencanvas.core.data.network.model.legalinfo

import com.google.gson.annotations.SerializedName

data class LegalInfoDto(
    @SerializedName("terms_of_service")
    val termsOfService: LegalDocDto,

    @SerializedName("privacy_policy")
    val privacyPolicy: LegalDocDto
)
