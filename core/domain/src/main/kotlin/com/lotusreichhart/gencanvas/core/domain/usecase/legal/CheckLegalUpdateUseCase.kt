package com.lotusreichhart.gencanvas.core.domain.usecase.legal

import com.lotusreichhart.gencanvas.core.domain.repository.LegalRepository
import javax.inject.Inject

class CheckLegalUpdateUseCase @Inject constructor(
    private val legalRepository: LegalRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return legalRepository.checkLegalUpdate()
    }
}