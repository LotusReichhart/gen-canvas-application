package com.lotusreichhart.gencanvas.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun saveOnboardingState(completed: Boolean)
    fun readOnboardingState(): Flow<Boolean>

    suspend fun saveLastBannerRefreshTime(time: Long)
    suspend fun getLastBannerRefreshTime(): Long
}