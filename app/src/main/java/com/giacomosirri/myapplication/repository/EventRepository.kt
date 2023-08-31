package com.giacomosirri.myapplication.repository

import androidx.annotation.WorkerThread
import com.giacomosirri.myapplication.data.dao.EventDAO
import com.giacomosirri.myapplication.data.entity.Event
import com.giacomosirri.myapplication.data.entity.Relationship
import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDAO: EventDAO) {
    val allItems: Flow<List<Event>> = eventDAO.getEvents()

    @WorkerThread
    fun getPotentialEventsOfUser(username : String) : Flow<Map<Event, Relationship.RelationshipType>> {
        return eventDAO.getPotentialEventsOfUser(username)
    }

    @WorkerThread
    suspend fun getEvent(id : Int) : Event {
        return eventDAO.getEvent(id)
    }

    @WorkerThread
    suspend fun insertEvent(event: Event) {
        eventDAO.insert(event)
    }

    @WorkerThread
    suspend fun deleteEvent(event: Event) {
        eventDAO.delete(event)
    }
}