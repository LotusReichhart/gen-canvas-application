package com.lotusreichhart.gencanvas.core.domain.usecase.legal

import com.lotusreichhart.gencanvas.core.domain.repository.LegalInfoRepository
import javax.inject.Inject

class CheckLegalUpdateUseCase @Inject constructor(
    private val legalInfoRepository: LegalInfoRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return legalInfoRepository.checkLegalUpdate()
    }
}