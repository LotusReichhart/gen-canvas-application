package com.lotusreichhart.gencanvas.core.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import com.lotusreichhart.gencanvas.core.domain.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingRepository {
    private object PreferencesKeys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val LAST_BANNER_REFRESH_TIME = longPreferencesKey("last_banner_refresh_time")
    }

    override suspend fun saveOnboardingState(completed: Boolean) {
        Timber.d("Bắt đầu lưu... Giá trị: $completed")
        try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.ONBOARDING_COMPLETED] = completed
                Timber.d("Đã gán giá trị bên trong edit")
            }
            Timber.d("Lưu DataStore THÀNH CÔNG.")
        } catch (e: Exception) {
            Timber.e(e, "Lưu DataStore THẤT BẠI!")
        }
    }

    override fun readOnboardingState(): Flow<Boolean> {
        Timber.d("Bắt đầu đọc Flow... (Hàm được gọi)")
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
                    Timber.e(exception, "Lỗi nghiêm trọng khi đọc DataStore!")
                    throw exception
                }
            }
            .map { preferences ->
                val value = preferences[PreferencesKeys.ONBOARDING_COMPLETED]
                Timber.d("Đã map giá trị. Giá trị thô: $value")
                value ?: false
            }
    }

    override suspend fun saveLastBannerRefreshTime(time: Long) {
        try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.LAST_BANNER_REFRESH_TIME] = time
            }
        } catch (e: Exception) {
            Timber.e(e, "Lưu refresh time thất bại")
        }
    }

    override suspend fun getLastBannerRefreshTime(): Long {
        return try {
            val preferences = dataStore.data.first()
            preferences[PreferencesKeys.LAST_BANNER_REFRESH_TIME] ?: 0L
        } catch (e: Exception) {
            Timber.e(e, "Đọc refresh time thất bại")
            0L
        }
    }
}