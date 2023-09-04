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
    fun getPotentialEventsOfUser(username : String) : Flow<Map<Event, Relationship.RelationshipType>> {
        return eventDAO.getPotentialEventsOfUser(username)
    }

    @WorkerThread
    suspend fun getEvent(id: Int) : Event {
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
        eventDAO.insertEvent(Event(name, date, location, organizer, dressCode,
            friendsAllowed, familyAllowed, partnersAllowed, colleaguesAllowed))
    }

    @WorkerThread
    suspend fun deleteEvent(eventId: Int) {
        eventDAO.deleteEvent(eventDAO.getEventFromId(eventId))
    }
}