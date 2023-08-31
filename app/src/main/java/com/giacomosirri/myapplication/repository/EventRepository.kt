package com.giacomosirri.myapplication.repository

import androidx.annotation.WorkerThread
import com.giacomosirri.myapplication.data.dao.EventDAO
import com.giacomosirri.myapplication.data.entity.Event
import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDAO: EventDAO) {
    val allItems: Flow<List<Event>> = eventDAO.getEvents()

    @WorkerThread
    suspend fun insertEvent(event: Event) {
        eventDAO.insert(event)
    }

    @WorkerThread
    suspend fun deleteEvent(event: Event) {
        eventDAO.delete(event)
    }

    @WorkerThread
    suspend fun deleteAllItems() {
        eventDAO.deleteAll()
    }
}