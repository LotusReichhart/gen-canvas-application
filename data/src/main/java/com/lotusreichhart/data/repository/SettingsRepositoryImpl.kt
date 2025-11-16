package com.lotusreichhart.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.lotusreichhart.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {
    private object PreferencesKeys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    override suspend fun saveOnboardingState(completed: Boolean) {
        Log.d("SaveOnboarding", "Bắt đầu lưu... Giá trị: $completed")
        try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.ONBOARDING_COMPLETED] = completed
                Log.d("SaveOnboarding", "Đã gán giá trị bên trong edit { ... }")
            }
            Log.d("SaveOnboarding", "Lưu DataStore THÀNH CÔNG.")

        } catch (e: Exception) {
            Log.e("SaveOnboarding", "Lưu DataStore THẤT BẠI!", e)
        }
    }

    override fun readOnboardingState(): Flow<Boolean> {
        Log.d("ReadOnboarding", "Bắt đầu đọc Flow... (Hàm được gọi)")
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    Log.w("ReadOnboarding", "Lỗi IOException khi đọc data, phát ra emptyPreferences()", exception)
                    emit(emptyPreferences())
                } else {
                    Log.e("ReadOnboarding", "Lỗi nghiêm trọng khi đọc DataStore!", exception)
                    throw exception
                }
            }
            .map { preferences ->
                val value = preferences[PreferencesKeys.ONBOARDING_COMPLETED]
                Log.d("ReadOnboarding", "Đã map giá trị. Giá trị thô: $value")
                value ?: false
            }
    }
}