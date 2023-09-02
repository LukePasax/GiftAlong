package com.giacomosirri.myapplication.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val context: Context) {
    // This ensures there is only one instance of SettingsRepository.
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("authentication")
        val USERNAME_KEY = stringPreferencesKey("username")
        val PASSWORD_KEY = stringPreferencesKey("password")
    }

    fun getUsername(): Flow<String> {
        return context.dataStore.data.map { preferences -> preferences[USERNAME_KEY] ?: "" }
    }

    fun getPassword(): Flow<String> {
        return context.dataStore.data.map { preferences -> preferences[PASSWORD_KEY] ?: "" }
    }

    suspend fun saveUsername(username: String) {
        context.dataStore.edit { preferences -> preferences[USERNAME_KEY] = username }
    }

    suspend fun savePassword(password: String) {
        context.dataStore.edit { preferences -> preferences[PASSWORD_KEY] = password }
    }
}