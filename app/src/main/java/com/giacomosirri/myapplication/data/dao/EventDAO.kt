package com.giacomosirri.myapplication.data.dao

import androidx.room.*
import com.giacomosirri.myapplication.data.entity.Event
import com.giacomosirri.myapplication.data.entity.Relationship
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDAO {
    @Query("SELECT * FROM events")
    fun getEvents(): Flow<List<Event>>

    @Query("SELECT * FROM events WHERE organizer = :username")
    fun getEventsOrganizedByUser(username : String) : Flow<List<Event>>

    @MapInfo(
        keyColumn = "id",
        keyTable = "events",
        valueColumn = "type",
        valueTable = "relationships"
    )
    @Query("SELECT events.*, relationships.type FROM users " +
            "JOIN relationships ON users.username = relationships.followed " +
            "JOIN events ON relationships.follower = events.organizer " +
            "WHERE users.username = :username")
    fun getPotentialEventsOfUser(username : String) : Flow<Map<Event,Relationship.RelationshipType>>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventFromId(id: Int) : Event

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Update
    suspend fun updateEvent(event: Event)
}