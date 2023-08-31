package com.giacomosirri.myapplication.data.dao

import androidx.room.*
import com.giacomosirri.myapplication.data.entity.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDAO {
    @Query("SELECT * FROM events")
    fun getEvents(): Flow<List<Event>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: Event)

    @Delete
    suspend fun delete(event: Event)

    @Query("DELETE FROM events")
    suspend fun deleteAll()
}