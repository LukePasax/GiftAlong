package com.giacomosirri.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.giacomosirri.myapplication.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

class SettingsViewModel(private val settingsRepository: SettingsRepository): ViewModel() {
    val isAutoAuthActive: Flow<Boolean> = settingsRepository.isAutomaticAuthenticationActive()
    val authenticatedUser: Flow<String> = settingsRepository.getAutomaticallyAuthenticatedUser()

    fun activateAutomaticAuthentication(username: String) {
        viewModelScope.launch {
            settingsRepository.activateAutomaticAuthentication(username)
        }
    }

    fun deactivateAutomaticAuthentication() {
        viewModelScope.launch {
            settingsRepository.deactivateAutomaticAuthentication()
        }
    }
}

class SettingsViewModelFactory(private val settingsRepository: SettingsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(settingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}