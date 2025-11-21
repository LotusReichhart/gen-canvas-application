package com.lotusreichhart.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import com.lotusreichhart.core.utils.logD
import com.lotusreichhart.core.utils.logE
import com.lotusreichhart.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {
    private object PreferencesKeys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val LAST_BANNER_REFRESH_TIME = longPreferencesKey("last_banner_refresh_time")
    }

    override suspend fun saveOnboardingState(completed: Boolean) {
        logD("Bắt đầu lưu... Giá trị: $completed")
        try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.ONBOARDING_COMPLETED] = completed
                logD("Đã gán giá trị bên trong edit { ... }")
            }
            logD("Lưu DataStore THÀNH CÔNG.")

        } catch (e: Exception) {
            logE("Lưu DataStore THẤT BẠI!", e)
        }
    }

    override fun readOnboardingState(): Flow<Boolean> {
        logD("Bắt đầu đọc Flow... (Hàm được gọi)")
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.w(
                        "ReadOnboarding",
                        "Lỗi IOException khi đọc data, phát ra emptyPreferences()",
                        exception
                    )
                    emit(emptyPreferences())
                } else {
                    logE("Lỗi nghiêm trọng khi đọc DataStore!", exception)
                    throw exception
                }
            }
            .map { preferences ->
                val value = preferences[PreferencesKeys.ONBOARDING_COMPLETED]
                logD("Đã map giá trị. Giá trị thô: $value")
                value ?: false
            }
    }

    override suspend fun saveLastBannerRefreshTime(time: Long) {
        try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.LAST_BANNER_REFRESH_TIME] = time
            }
        } catch (e: Exception) {
            logE("Lưu refresh time thất bại", e)
        }
    }

    override suspend fun getLastBannerRefreshTime(): Long {
        return try {
            val preferences = dataStore.data.first()
            preferences[PreferencesKeys.LAST_BANNER_REFRESH_TIME] ?: 0L
        } catch (e: Exception) {
            logE("Đọc refresh time thất bại", e)
            0L
        }
    }
}