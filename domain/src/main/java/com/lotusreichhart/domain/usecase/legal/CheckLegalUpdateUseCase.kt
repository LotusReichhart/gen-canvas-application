package com.lotusreichhart.domain.usecase.legal

import com.lotusreichhart.domain.repository.LegalRepository

class CheckLegalUpdateUseCase(
    private val legalRepository: LegalRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return legalRepository.checkLegalUpdate()
    }
}