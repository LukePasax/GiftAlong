package com.giacomosirri.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.giacomosirri.myapplication.data.entity.Event
import com.giacomosirri.myapplication.data.entity.Relationship
import com.giacomosirri.myapplication.repository.EventRepository
import com.giacomosirri.myapplication.repository.ItemRepository
import com.giacomosirri.myapplication.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    private fun isInvitedToEvent(type : Relationship.RelationshipType, event : Event) : Boolean {
        return when(type) {
            Relationship.RelationshipType.FRIEND -> event.friendsAllowed
            Relationship.RelationshipType.FAMILY -> event.familyAllowed
            Relationship.RelationshipType.PARTNER -> event.partnersAllowed
            Relationship.RelationshipType.COLLEAGUE -> event.colleaguesAllowed
        }
    }

    fun getEventsOfUser(username : String): Flow<Map<Event, Relationship.RelationshipType>> {
        val potentialEvents = eventRepository.getPotentialEventsOfUser(username)
        return potentialEvents.map { events ->
            events.filter {
                isInvitedToEvent(it.value, it.key)
            }
        }
    }

    fun registerUser(username: String, password: String, name: String, surname: String, birthday: Date) = viewModelScope.launch {
        userRepository.insertUser(username, password, name, surname, birthday)
    }

    fun loginUser(username: String, password: String) = viewModelScope.launch {
        userRepository.getUser(username, password)
    }

    fun getItemsOfUser(username: String) = itemRepository.getItemsOfUser(username)


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