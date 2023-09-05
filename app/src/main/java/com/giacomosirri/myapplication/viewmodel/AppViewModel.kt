package com.giacomosirri.myapplication.viewmodel

import androidx.lifecycle.*
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.data.entity.Event
import com.giacomosirri.myapplication.data.entity.Item
import com.giacomosirri.myapplication.data.entity.Relationship
import com.giacomosirri.myapplication.data.entity.User
import com.giacomosirri.myapplication.repository.EventRepository
import com.giacomosirri.myapplication.repository.ItemRepository
import com.giacomosirri.myapplication.repository.UserRepository
import com.giacomosirri.myapplication.ui.AppContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.*

class AppViewModel(
    private val eventRepository: EventRepository,
    private val itemRepository: ItemRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    // User functions
    fun registerUser(
        username: String,
        password: String,
        name: String,
        surname: String,
        image: Int? = null,
        birthday: Date
    ) {
        viewModelScope.launch {
            userRepository.insertUser(username, password, name, surname, image?: R.drawable.placeholder, birthday)
        }
    }

    suspend fun loginUser(username: String, password: String): Boolean {
        return withContext(viewModelScope.coroutineContext) {
            userRepository.getUser(username, password)
        }
    }

    fun unregisterUser(username: String) {
        viewModelScope.launch {
            userRepository.deleteUser(username)
        }
    }

    suspend fun usernameExists(username: String): Boolean {
        return userRepository.getUser(username)
    }

    suspend fun getSubscriptionDate(username: String): Date {
        return userRepository.getSubscriptionDate(username)
    }

    // Event functions
    fun getAllEvents(): Flow<List<Event>> = eventRepository.allItems

    fun getEventsOrganizedByUser(username : String) : Flow<List<Event>> {
        return eventRepository.getEventsOrganizedByUser(username)
    }

    fun addEvent(
        name: String,
        date: Date,
        location: String?,
        organizer: String,
        dressCode: String?,
        friendsAllowed: Boolean,
        familyAllowed: Boolean,
        partnersAllowed: Boolean,
        colleaguesAllowed: Boolean
    ) {
        viewModelScope.launch {
            eventRepository.insertEvent(
                name, date, location, organizer, dressCode,
                friendsAllowed, familyAllowed, partnersAllowed, colleaguesAllowed
            )
        }
    }

    fun deleteEvent(eventId: Int) = viewModelScope.launch {
        eventRepository.deleteEvent(eventId)
    }

    fun updateEvent(
        id: Int,
        name: String? = null,
        date: Date? = null,
        location: String? = null,
        dressCode: String? = null,
        friendsAllowed: Boolean? = null,
        partnersAllowed: Boolean? = null,
        familyAllowed: Boolean? = null,
        colleaguesAllowed: Boolean? = null
    ) {
        viewModelScope.launch {
            eventRepository.updateEvent(id, name, date, location, dressCode,
                friendsAllowed, partnersAllowed, familyAllowed, colleaguesAllowed)
        }
    }

    private fun isInvitedToEvent(type : Relationship.RelationshipType, event : Event) : Boolean {
        return when(type) {
            Relationship.RelationshipType.Friend -> event.friendsAllowed
            Relationship.RelationshipType.Family -> event.familyAllowed
            Relationship.RelationshipType.Partner -> event.partnersAllowed
            Relationship.RelationshipType.Colleague -> event.colleaguesAllowed
        }
    }

    fun getEventsOfUser(username : String): Flow<Set<Event>> {
        val potentialEvents = eventRepository.getPotentialEventsOfUser(username)
        return potentialEvents.map { map ->
            map.filter { (event, type) ->
                isInvitedToEvent(type, event)
            }.keys
        }
    }

    fun getCommonEvents(username: String) : Flow<Set<Event>> {
        val currentUserEvents = getEventsOfUser(AppContext.getCurrentUser())
        val otherUserEvents = getEventsOfUser(username)
        return currentUserEvents.combine(otherUserEvents) { current, other ->
            current.intersect(other)
        }
    }

    // Item functions
    fun getItemsOfUser(username: String): Flow<List<Item>> = itemRepository.getItemsOfUser(username)

    suspend fun getItemFromId(id: Int): Item = itemRepository.getItemFromId(id)

    fun addItem(
        name: String,
        description: String? = null,
        url: String? = null,
        image: Int? = null,
        priceL: Int? = null,
        priceU: Int? = null,
        listedBy: String
    ) {
        viewModelScope.launch {
            itemRepository.insertItem(name, description, url, image?: R.drawable.placeholder, priceL, priceU, listedBy)
        }
    }

    fun deleteItem(id : Int) = viewModelScope.launch {
        itemRepository.deleteItem(id)
    }

    fun updateItem(
        id: Int,
        bought: Boolean? = null,
        name: String? = null,
        description: String? = null,
        url: String? = null,
        image: Int? = null,
        priceL: Int? = null,
        priceU: Int? = null,
        reservedBy: String? = null
    ) {
        viewModelScope.launch {
            itemRepository.updateItem(id, bought, name, description, url, image, priceL, priceU, reservedBy)
        }
    }

    suspend fun getItemNameFromId(id: Int): String {
        return itemRepository.getItemFromId(id).name
    }

    suspend fun getItemDescriptionFromId(id: Int): String? {
        return itemRepository.getItemFromId(id).description
    }

    suspend fun getItemLinkFromId(id: Int): String? {
        return itemRepository.getItemFromId(id).url
    }

    suspend fun getItemUpperBoundPriceFromId(id: Int): Int? {
        return itemRepository.getItemFromId(id).priceUpperBound
    }

    suspend fun getItemLowerBoundPriceFromId(id: Int): Int? {
        return itemRepository.getItemFromId(id).priceLowerBound
    }

    suspend fun getEventNameFromId(id: Int): String {
        return eventRepository.getEventFromId(id).name
    }

    suspend fun getEventDressCodeFromId(id: Int): String? {
        return eventRepository.getEventFromId(id).dressCode
    }

    suspend fun getEventDateFromId(id: Int): Date {
        return eventRepository.getEventFromId(id).date
    }

    suspend fun getFriendsParticipationToEventFromId(id: Int): Boolean {
        return eventRepository.getEventFromId(id).friendsAllowed
    }

    suspend fun getPartnersParticipationToEventFromId(id: Int): Boolean {
        return eventRepository.getEventFromId(id).partnersAllowed
    }

    suspend fun getFamilyParticipationToEventFromId(id: Int): Boolean {
        return eventRepository.getEventFromId(id).familyAllowed
    }

    suspend fun getColleaguesParticipationToEventFromId(id: Int): Boolean {
        return eventRepository.getEventFromId(id).colleaguesAllowed
    }

    fun getRelationshipsOfUser(username: String): Flow<List<Relationship>> = userRepository.getRelationshipsOfUser(username)

    fun getUsersMatchingPattern(query: String): Flow<List<User>> = userRepository.getAllUsers(query)

    fun updateRelationship(follower: String, followed: String, relationship: String) {
        if (relationship == "None") {
            viewModelScope.launch { userRepository.deleteRelationship(follower, followed) }
        } else {
            viewModelScope.launch { userRepository.updateRelationship(follower, followed, relationship) }
        }
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