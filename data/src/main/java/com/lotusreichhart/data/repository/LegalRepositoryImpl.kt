package com.lotusreichhart.data.repository

import com.lotusreichhart.data.local.database.dao.LegalDao
import com.lotusreichhart.data.mapper.toDomain
import com.lotusreichhart.data.mapper.toLocal
import com.lotusreichhart.data.remote.service.LegalApiService
import com.lotusreichhart.data.utils.safeApiCallData
import com.lotusreichhart.domain.entity.LegalInfoEntity
import com.lotusreichhart.domain.repository.LegalRepository
import javax.inject.Inject

class LegalRepositoryImpl @Inject constructor(
    private val apiService: LegalApiService,
    private val legalDao: LegalDao
) : LegalRepository {

    override suspend fun getLegalInfo(): Result<LegalInfoEntity> {
        val localData = legalDao.getLegalInfo()
        if (localData != null) {
            return Result.success(localData.toDomain())
        }

        return safeApiCallData {
            apiService.getLegalInfo()
        }.map { dto ->
            legalDao.saveLegalInfo(dto.toLocal())
            dto.toDomain()
        }
    }

    override suspend fun checkLegalUpdate(): Result<Boolean> {
        val currentLocal = legalDao.getLegalInfo()

        val remoteResult = safeApiCallData { apiService.getLegalInfo() }

        return remoteResult.map { remoteDto ->
            var hasUpdate = false

            if (currentLocal == null) {
                legalDao.saveLegalInfo(remoteDto.toLocal())
                hasUpdate = false
            } else {
                val isTermsChanged = remoteDto.termsOfService.version != currentLocal.termsVersion
                val isPrivacyChanged = remoteDto.privacyPolicy.version != currentLocal.privacyVersion

                if (isTermsChanged || isPrivacyChanged) {
                    legalDao.saveLegalInfo(remoteDto.toLocal())
                    hasUpdate = true
                }
            }

            hasUpdate
        }
    }
}