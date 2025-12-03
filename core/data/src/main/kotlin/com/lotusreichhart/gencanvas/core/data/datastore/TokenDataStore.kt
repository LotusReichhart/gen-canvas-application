package com.lotusreichhart.gencanvas.core.data.datastore

import javax.inject.Inject

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Singleton

@Singleton
class TokenDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val _accessToken = MutableStateFlow<String?>(null)
    private val _refreshToken = MutableStateFlow<String?>(null)

    companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Timber.d("TokenDataStore: Đang tải token từ DataStore...")
                val access = dataStore.data.map { it[ACCESS_TOKEN_KEY] }.first()
                val refresh = dataStore.data.map { it[REFRESH_TOKEN_KEY] }.first()

                _accessToken.value = access
                _refreshToken.value = refresh

                Timber.d("TokenDataStore Init: Load xong. Access: ${access.toLog()}, Refresh: ${refresh.toLog()}")
            } catch (e: Exception) {
                Timber.e(e, "TokenDataStore: Lỗi khi tải token từ DataStore")
            }
        }
    }

    fun getAccessTokenValue(): String? {
        val token = _accessToken.value
        Timber.d("getAccessTokenValue called: ${token.toLog()}")
        return token
    }

    fun getRefreshTokenValue(): String? {
        val token = _refreshToken.value
        Timber.d("getRefreshTokenValue called: ${token.toLog()}")
        return token
    }

    suspend fun saveTokens(access: String, refresh: String) {
        try {
            Timber.d("saveTokens: Bắt đầu lưu... Access: ${access.toLog()}, Refresh: ${refresh.toLog()}")
            dataStore.edit { preferences ->
                preferences[ACCESS_TOKEN_KEY] = access
                preferences[REFRESH_TOKEN_KEY] = refresh
            }

            _accessToken.value = access
            _refreshToken.value = refresh
            Timber.d("saveTokens: Đã lưu THÀNH CÔNG vào DataStore & Cache")
        } catch (e: Exception) {
            Timber.e(e, "saveTokens: Lỗi khi lưu token")
            throw e
        }
    }

    suspend fun updateAccessToken(access: String) {
        try {
            Timber.d("updateAccessToken: Đang cập nhật Access mới: ${access.toLog()}")
            dataStore.edit { preferences ->
                preferences[ACCESS_TOKEN_KEY] = access
            }
            _accessToken.value = access
            Timber.d("updateAccessToken: Cập nhật THÀNH CÔNG")
        } catch (e: Exception) {
            Timber.e(e, "updateAccessToken: Lỗi khi cập nhật")
            throw e
        }
    }

    suspend fun clearTokens() {
        try {
            Timber.d("clearTokens: Đang xóa token...")
            dataStore.edit { preferences ->
                preferences.remove(ACCESS_TOKEN_KEY)
                preferences.remove(REFRESH_TOKEN_KEY)
            }
            _accessToken.value = null
            _refreshToken.value = null
            Timber.d("clearTokens: Đã xóa THÀNH CÔNG (Logout)")
        } catch (e: Exception) {
            Timber.e(e, "clearTokens: Lỗi khi xóa")
            throw e
        }
    }

    private fun String?.toLog(): String {
        return if (this.isNullOrBlank()) "NULL/EMPTY" else "${this.take(10)}..."
    }
}