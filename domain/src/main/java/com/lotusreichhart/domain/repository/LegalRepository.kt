package com.lotusreichhart.domain.repository

import com.lotusreichhart.domain.entity.LegalInfoEntity

interface LegalRepository {
    suspend fun getLegalInfo(): Result<LegalInfoEntity>

    suspend fun checkLegalUpdate(): Result<Boolean>
}