package com.lotusreichhart.gencanvas.core.domain.usecase.legal

import com.lotusreichhart.gencanvas.core.domain.repository.LegalInfoRepository
import com.lotusreichhart.gencanvas.core.model.legalinfo.LegalInfo
import javax.inject.Inject

class GetLegalInfoUseCase @Inject constructor(
    private val legalInfoRepository: LegalInfoRepository
) {
    suspend operator fun invoke(): Result<LegalInfo> {
        return legalInfoRepository.getLegalInfo()
    }
}