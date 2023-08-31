package com.giacomosirri.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.giacomosirri.myapplication.data.entity.Event
import androidx.lifecycle.viewModelScope
import com.giacomosirri.myapplication.repository.EventRepository
import com.giacomosirri.myapplication.repository.ItemRepository
import com.giacomosirri.myapplication.repository.UserRepository
import kotlinx.coroutines.launch
import java.util.*

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

    fun registerUser(username: String, password: String, name: String, surname: String, birthday: Date) = viewModelScope.launch {
        userRepository.insertUser(username, password, name, surname, birthday)
    }

    fun loginUser(username: String, password: String) = viewModelScope.launch {
        userRepository.getUser(username, password)
    }

    fun getItemsOfUser(username: String) = viewModelScope.launch {
        itemRepository.getItemsOfUser(username)
    }

    fun addItem(name : String, description : String? = null, url : String? = null, image : String? = null, priceL : Double? = null, priceU : Double? = null, listedBy : String) = viewModelScope.launch {
        itemRepository.insertItem(name, description, url, image, priceL, priceU, listedBy)
    }

    fun deleteItem(id : Int) = viewModelScope.launch {
        itemRepository.deleteItem(id)
    }

    fun updateItem(id : Int, bought : Boolean? = null, name : String? = null, description : String? = null, url : String? = null, image : String? = null, priceL : Double? = null, priceU : Double? = null, reservedBy : String? = null, listedBy : String? = null) = viewModelScope.launch {
        itemRepository.updateItem(id, bought, name, description, url, image, priceL, priceU, reservedBy, listedBy)
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