package com.lotusreichhart.gencanvas.core.data.repository

import com.lotusreichhart.gencanvas.core.data.database.dao.LegalInfoDao
import com.lotusreichhart.gencanvas.core.data.mapper.toEntity
import com.lotusreichhart.gencanvas.core.data.mapper.toModel
import com.lotusreichhart.gencanvas.core.data.network.service.LegalInfoApiService
import com.lotusreichhart.gencanvas.core.data.network.util.safeApiCallData
import com.lotusreichhart.gencanvas.core.domain.repository.LegalInfoRepository
import com.lotusreichhart.gencanvas.core.model.legalinfo.LegalInfo
import javax.inject.Inject

class LegalInfoRepositoryImpl @Inject constructor(
    private val apiService: LegalInfoApiService,
    private val dao: LegalInfoDao
) : LegalInfoRepository {

    override suspend fun getLegalInfo(): Result<LegalInfo> {
        val legalInfoEntity = dao.getLegalInfo()
        if (legalInfoEntity != null) {
            return Result.success(legalInfoEntity.toModel())
        }

        return safeApiCallData {
            apiService.getLegalInfo()
        }.map { dto ->
            dao.saveLegalInfo(dto.toEntity())
            dto.toModel()
        }
    }

    override suspend fun checkLegalUpdate(): Result<Boolean> {
        val currentEntity = dao.getLegalInfo()

        val remoteResult = safeApiCallData { apiService.getLegalInfo() }

        return remoteResult.map { remoteDto ->
            var hasUpdate = false

            if (currentEntity == null) {
                dao.saveLegalInfo(remoteDto.toEntity())
                hasUpdate = false
            } else {
                val isTermsChanged = remoteDto.termsOfService.version != currentEntity.termsVersion
                val isPrivacyChanged =
                    remoteDto.privacyPolicy.version != currentEntity.privacyVersion

                if (isTermsChanged || isPrivacyChanged) {
                    dao.saveLegalInfo(remoteDto.toEntity())
                    hasUpdate = true
                }
            }

            hasUpdate
        }
    }
}