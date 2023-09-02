package com.giacomosirri.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.giacomosirri.myapplication.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

class SettingsViewModel(private val settingsRepository: SettingsRepository): ViewModel() {
    val username: Flow<String> = settingsRepository.getUsername()
    val password: Flow<String> = settingsRepository.getPassword()

    fun automaticLogin(username: String, password: String) {
        viewModelScope.launch {
            settingsRepository.saveUsername(username)
            settingsRepository.savePassword(password)
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