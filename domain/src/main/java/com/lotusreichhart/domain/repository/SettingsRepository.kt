package com.lotusreichhart.domain.repositories

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun saveOnboardingState(completed: Boolean)
    fun readOnboardingState(): Flow<Boolean>
}