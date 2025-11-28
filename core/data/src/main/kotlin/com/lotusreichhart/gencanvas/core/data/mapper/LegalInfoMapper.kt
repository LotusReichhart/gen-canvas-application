package com.lotusreichhart.gencanvas.core.data.mapper

import com.lotusreichhart.gencanvas.core.data.database.model.legalinfo.LegalInfoEntity
import com.lotusreichhart.gencanvas.core.data.network.model.legalinfo.LegalInfoDto
import com.lotusreichhart.gencanvas.core.model.legalinfo.LegalInfo

fun LegalInfoDto.toModel(): LegalInfo {
    return LegalInfo(
        termsUrl = this.termsOfService.url,
        termsVersion = this.termsOfService.version,
        privacyUrl = this.privacyPolicy.url,
        privacyVersion = this.privacyPolicy.version
    )
}

fun LegalInfoDto.toEntity(): LegalInfoEntity {
    return LegalInfoEntity(
        id = 0,
        termsUrl = this.termsOfService.url,
        termsVersion = this.termsOfService.version,
        privacyUrl = this.privacyPolicy.url,
        privacyVersion = this.privacyPolicy.version
    )
}

fun LegalInfoEntity.toModel(): LegalInfo {
    return LegalInfo(
        termsUrl = this.termsUrl,
        termsVersion = this.termsVersion,
        privacyUrl = this.privacyUrl,
        privacyVersion = this.privacyVersion
    )
}