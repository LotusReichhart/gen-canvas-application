package com.lotusreichhart.gencanvas.core.domain.usecase.legal

import com.lotusreichhart.gencanvas.core.domain.repository.LegalRepository
import com.lotusreichhart.gencanvas.core.model.legalinfo.LegalInfo
import javax.inject.Inject

class GetLegalInfoUseCase @Inject constructor(
    private val legalRepository: LegalRepository
) {
    suspend operator fun invoke(): Result<LegalInfo> {
        return legalRepository.getLegalInfo()
    }
}