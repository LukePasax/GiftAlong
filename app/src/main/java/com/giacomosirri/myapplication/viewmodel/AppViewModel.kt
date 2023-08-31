package com.giacomosirri.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.giacomosirri.myapplication.data.entity.Event
import androidx.lifecycle.viewModelScope
import com.giacomosirri.myapplication.repository.EventRepository
import com.giacomosirri.myapplication.repository.ItemRepository
import com.giacomosirri.myapplication.repository.UserRepository
import kotlinx.coroutines.launch

class AppViewModel(
    private val eventRepository: EventRepository,
    private val itemRepository: ItemRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val allItems = eventRepository.allItems

    fun addEvent(event: Event) = viewModelScope.launch {
        eventRepository.insertEvent(event)
    }

    fun removeEvent(event: Event) = viewModelScope.launch {
        eventRepository.deleteEvent(event)
    }

    fun clearItems() = viewModelScope.launch {
        eventRepository.deleteAllItems()
    }
}

class AppViewModelFactory(
    private val eventRepository: EventRepository,
    private val itemRepository: ItemRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(eventRepository, itemRepository, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}