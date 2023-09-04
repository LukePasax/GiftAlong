package com.giacomosirri.myapplication.data.dao

import androidx.room.*
import com.giacomosirri.myapplication.data.entity.Relationship
import kotlinx.coroutines.flow.Flow

@Dao
interface RelationshipDAO {
    @Query("SELECT * FROM relationships WHERE follower = :username")
    fun getRelationshipsOfUser(username: String): Flow<List<Relationship>>

    @Query("SELECT * FROM relationships WHERE follower = :follower AND followed = :followed")
    suspend fun getRelationshipsBetweenUsers(follower: String, followed: String): Relationship?

    @Update
    fun updateRelationship(relationship: Relationship)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertRelationship(relationship: Relationship)

    @Delete
    fun deleteRelationships(relationship: Relationship)
}