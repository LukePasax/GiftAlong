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
        val SAVED_USERNAME = stringPreferencesKey("saved_username")
    }

    fun isAutomaticAuthenticationActive(): Flow<Boolean> {
        return context.dataStore.data.map { preferences -> preferences[SAVED_USERNAME] != "" }
    }

    fun getAutomaticallyAuthenticatedUser(): Flow<String> {
        return context.dataStore.data.map { preferences -> preferences[SAVED_USERNAME] ?: "" }
    }

    suspend fun activateAutomaticAuthentication(username: String) {
        context.dataStore.edit { preferences -> preferences[SAVED_USERNAME] = username }
    }

    suspend fun deactivateAutomaticAuthentication() {
        context.dataStore.edit { preferences -> preferences[SAVED_USERNAME] = "" }
    }
}