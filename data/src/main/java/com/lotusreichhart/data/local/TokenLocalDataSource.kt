package com.lotusreichhart.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenLocalDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val _accessToken = MutableStateFlow<String?>(null)
    val accessToken: StateFlow<String?> = _accessToken.asStateFlow()

    private val _refreshToken = MutableStateFlow<String?>(null)
    val refreshToken: StateFlow<String?> = _refreshToken.asStateFlow()

    fun getAccessTokenValue(): String? = _accessToken.value
    fun getRefreshTokenValue(): String? = _refreshToken.value

    companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            _accessToken.value = dataStore.data.map { it[ACCESS_TOKEN_KEY] }.first()
            _refreshToken.value = dataStore.data.map { it[REFRESH_TOKEN_KEY] }.first()
        }
    }

    suspend fun saveTokens(access: String, refresh: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = access
            preferences[REFRESH_TOKEN_KEY] = refresh
        }
        _accessToken.value = access
        _refreshToken.value = refresh
    }

    suspend fun updateAccessToken(access: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = access
        }
        _accessToken.value = access
    }

    suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
        _accessToken.value = null
        _refreshToken.value = null
    }
}