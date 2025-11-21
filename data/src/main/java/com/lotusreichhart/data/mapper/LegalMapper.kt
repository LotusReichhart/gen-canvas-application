package com.lotusreichhart.data.mapper

import com.lotusreichhart.data.local.database.model.LegalLocal
import com.lotusreichhart.data.remote.dto.legal.LegalInfoDTO
import com.lotusreichhart.domain.entity.LegalInfoEntity

fun LegalInfoDTO.toDomain(): LegalInfoEntity {
    return LegalInfoEntity(
        termsUrl = this.termsOfService.url,
        termsVersion = this.termsOfService.version,
        privacyUrl = this.privacyPolicy.url,
        privacyVersion = this.privacyPolicy.version
    )
}

fun LegalInfoDTO.toLocal(): LegalLocal {
    return LegalLocal(
        id = 0,
        termsUrl = this.termsOfService.url,
        termsVersion = this.termsOfService.version,
        privacyUrl = this.privacyPolicy.url,
        privacyVersion = this.privacyPolicy.version
    )
}

fun LegalLocal.toDomain(): LegalInfoEntity {
    return LegalInfoEntity(
        termsUrl = this.termsUrl,
        termsVersion = this.termsVersion,
        privacyUrl = this.privacyUrl,
        privacyVersion = this.privacyVersion
    )
}