package com.giacomosirri.myapplication.data.dao

import androidx.room.*
import com.giacomosirri.myapplication.data.entity.Relationship
import com.giacomosirri.myapplication.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface RelationshipDAO {
    @MapInfo(
        keyColumn = "username",
        keyTable = "users",
        valueColumn = "type",
        valueTable = "relationships"
    )
    @Query("SELECT users.*, relationships.type FROM users JOIN relationships ON " +
            "users.username = relationships.followed WHERE follower = :username")
    fun getRelationshipsOfUser(username: String): Flow<Map<User, Relationship.RelationshipType>>

    @Query("SELECT * FROM relationships WHERE follower = :follower AND followed = :followed")
    suspend fun getRelationshipsBetweenUsers(follower: String, followed: String): Relationship?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRelationship(relationship: Relationship)

    @Delete
    suspend fun deleteRelationships(relationship: Relationship)
}