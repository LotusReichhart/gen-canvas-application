package com.lotusreichhart.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun saveOnboardingState(completed: Boolean)
    fun readOnboardingState(): Flow<Boolean>
}