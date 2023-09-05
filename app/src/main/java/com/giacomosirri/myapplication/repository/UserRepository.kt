package com.giacomosirri.myapplication.repository

import androidx.annotation.WorkerThread
import com.giacomosirri.myapplication.data.dao.RelationshipDAO
import com.giacomosirri.myapplication.data.dao.UserDAO
import com.giacomosirri.myapplication.data.entity.Relationship
import com.giacomosirri.myapplication.data.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import java.util.*

class UserRepository(private val userDAO: UserDAO, private val relationshipDAO: RelationshipDAO) {
    @WorkerThread
    suspend fun insertUser(username: String, password: String, name: String, surname: String, image : Int, birthday: Date) {
        userDAO.insertUser(User(username, password, name, surname, birthday, image, Date()))
    }

    @WorkerThread
    suspend fun getUser(username: String, password: String): Boolean = userDAO.getUser(username, password) != null

    @WorkerThread
    suspend fun getUser(username: String): Boolean = userDAO.getUser(username) != null

    @WorkerThread
    suspend fun getSubscriptionDate(username: String): Date = userDAO.getUser(username)!!.subscriptionDate

    @WorkerThread
    suspend fun deleteUser(username: String) {
        userDAO.deleteUser(userDAO.getUser(username)!!)
    }

    fun getRelationshipsOfUser(username: String): Flow<List<Relationship>> =
        relationshipDAO.getRelationshipsOfUser(username)

    private suspend fun getRelationshipsBetweenUsers(follower: String, followed: String): Relationship? =
        relationshipDAO.getRelationshipsBetweenUsers(follower, followed)

    fun getAllUsers(query: String): Flow<List<User>> = userDAO.getAllUsers(query)

    suspend fun updateRelationship(follower: String, followed: String, newRelationship: String) {
        // Updating a relationships means either adding it or updating if it already existed.
        relationshipDAO.insertRelationship(Relationship(follower, followed, enumValueOf(newRelationship)))
    }

    suspend fun deleteRelationship(follower: String, followed: String) {
        val currentRelationship = getRelationshipsBetweenUsers(follower, followed)
        // If the current relationship is already null, there is nothing to remove.
        if (currentRelationship != null) {
            relationshipDAO.deleteRelationships(Relationship(follower, followed, currentRelationship.type))
        }
    }
}