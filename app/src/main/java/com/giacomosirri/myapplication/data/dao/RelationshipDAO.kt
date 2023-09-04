package com.giacomosirri.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.giacomosirri.myapplication.data.entity.Relationship
import kotlinx.coroutines.flow.Flow

@Dao
interface RelationshipDAO {
    @Query("SELECT * FROM relationships WHERE follower = :username")
    fun getRelationshipsOfUser(username: String): Flow<List<Relationship>>
}