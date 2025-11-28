package com.lotusreichhart.gencanvas.core.data.datastore

import javax.inject.Inject

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class TokenDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val externalScope: CoroutineScope
) {
    private val _accessToken = MutableStateFlow<String?>(null)
    private val _refreshToken = MutableStateFlow<String?>(null)

    fun getAccessTokenValue(): String? = _accessToken.value
    fun getRefreshTokenValue(): String? = _refreshToken.value

    companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    init {
        externalScope.launch {
            try {
                _accessToken.value = dataStore.data.map { it[ACCESS_TOKEN_KEY] }.first()
                _refreshToken.value = dataStore.data.map { it[REFRESH_TOKEN_KEY] }.first()
            } catch (e: Exception) {
                Timber.e(e, "Error loading tokens from DataStore")
            }
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