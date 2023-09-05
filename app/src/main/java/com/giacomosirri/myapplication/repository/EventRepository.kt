package com.giacomosirri.myapplication.repository

import androidx.annotation.WorkerThread
import com.giacomosirri.myapplication.data.dao.EventDAO
import com.giacomosirri.myapplication.data.entity.Event
import com.giacomosirri.myapplication.data.entity.Relationship
import kotlinx.coroutines.flow.Flow
import java.util.*

class EventRepository(private val eventDAO: EventDAO) {
    val allItems: Flow<List<Event>> = eventDAO.getEvents()

    @WorkerThread
    fun getEventsOrganizedByUser(username: String): Flow<List<Event>> {
        return eventDAO.getEventsOrganizedByUser(username)
    }

    @WorkerThread
    fun getPotentialEventsOfUser(username: String): Flow<Map<Event, Relationship.RelationshipType>> {
        return eventDAO.getPotentialEventsOfUser(username)
    }

    @WorkerThread
    suspend fun getEventFromId(id: Int): Event {
        return eventDAO.getEventFromId(id)
    }

    @WorkerThread
    suspend fun insertEvent(
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
        eventDAO.insertEvent(Event(null, name, date, location, organizer, dressCode,
            friendsAllowed, familyAllowed, partnersAllowed, colleaguesAllowed))
    }

    @WorkerThread
    suspend fun deleteEvent(eventId: Int) {
        eventDAO.deleteEvent(eventDAO.getEventFromId(eventId))
    }

    @WorkerThread
    suspend fun updateEvent(
        id: Int,
        name: String? = null,
        date: Date? = null,
        location: String? = null,
        dressCode: String? = null,
        friendsAllowed: Boolean? = null,
        familyAllowed: Boolean? = null,
        partnersAllowed: Boolean? = null,
        colleaguesAllowed: Boolean? = null
    ) {
        val oldEvent = eventDAO.getEventFromId(id)
        val event = Event(
            id = id,
            name = name ?: oldEvent.name,
            date = date ?: oldEvent.date,
            location = location ?: oldEvent.location,
            // If the user leaves this field blank in the UI, it means that it must be brought back to null.
            dressCode = if (dressCode == "") null else dressCode ?: oldEvent.dressCode,
            // The organizer can never change.
            organizer = oldEvent.organizer,
            friendsAllowed = friendsAllowed ?: oldEvent.friendsAllowed,
            familyAllowed = familyAllowed ?: oldEvent.familyAllowed,
            partnersAllowed = partnersAllowed ?: oldEvent.partnersAllowed,
            colleaguesAllowed = colleaguesAllowed ?: oldEvent.colleaguesAllowed
        )
        eventDAO.updateEvent(event)
    }
}