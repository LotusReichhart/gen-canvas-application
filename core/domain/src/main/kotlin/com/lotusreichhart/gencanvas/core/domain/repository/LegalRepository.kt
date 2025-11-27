package com.lotusreichhart.gencanvas.core.domain.repository

import com.lotusreichhart.gencanvas.core.model.legalinfo.LegalInfo

interface LegalRepository {
    suspend fun getLegalInfo(): Result<LegalInfo>

    suspend fun checkLegalUpdate(): Result<Boolean>
}