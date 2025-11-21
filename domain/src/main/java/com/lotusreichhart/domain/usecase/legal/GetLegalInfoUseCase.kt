package com.lotusreichhart.domain.usecase.legal

import com.lotusreichhart.domain.entity.LegalInfoEntity
import com.lotusreichhart.domain.repository.LegalRepository

class GetLegalInfoUseCase(
    private val legalRepository: LegalRepository
) {
    suspend operator fun invoke(): Result<LegalInfoEntity> {
        return legalRepository.getLegalInfo()
    }
}