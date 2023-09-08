package com.giacomosirri.myapplication.repository

import androidx.annotation.WorkerThread
import com.giacomosirri.myapplication.data.dao.RelationshipDAO
import com.giacomosirri.myapplication.data.dao.UserDAO
import com.giacomosirri.myapplication.data.entity.Relationship
import com.giacomosirri.myapplication.data.entity.User
import kotlinx.coroutines.flow.Flow
import java.util.*

class UserRepository(private val userDAO: UserDAO, private val relationshipDAO: RelationshipDAO) {
    @WorkerThread
    suspend fun insertUser(username: String, password: String, name: String, surname: String, imageUri: String?, birthday: Date) {
        userDAO.insertUser(User(username, password, name, surname, birthday, imageUri, Date()))
    }

    @WorkerThread
    suspend fun updateImage(username: String, imageUri: String) {
        val oldUser = userDAO.getUser(username)!!
        val newUser = User(oldUser.username, oldUser.password, oldUser.name, oldUser.surname,
                            oldUser.birthday, imageUri, oldUser.subscriptionDate)
        userDAO.updateUser(newUser)
    }

    @WorkerThread
    suspend fun getUser(username: String, password: String): Boolean = userDAO.getUser(username, password) != null

    @WorkerThread
    suspend fun getUser(username: String): Boolean = userDAO.getUser(username) != null

    @WorkerThread
    suspend fun getSubscriptionDate(username: String): Date = userDAO.getUser(username)!!.subscriptionDate

    @WorkerThread
    suspend fun getProfilePic(username: String): String? = userDAO.getUser(username)?.imageUri

    @WorkerThread
    suspend fun deleteUser(username: String) {
        userDAO.deleteUser(userDAO.getUser(username)!!)
    }

    fun getRelationshipsOfUser(username: String): Flow<Map<User, Relationship.RelationshipType>> =
        relationshipDAO.getRelationshipsOfUser(username)

    private suspend fun getRelationshipsBetweenUsers(follower: String, followed: String): Relationship? =
        relationshipDAO.getRelationshipsBetweenUsers(follower, followed)

    fun getAllUsers(query: String): Flow<List<User>> = userDAO.getAllUsers(query)

    suspend fun updateRelationship(follower: String, followed: String, newRelationship: String) {
        // Updating a relationships means either adding it or updating if it already existed.
        relationshipDAO.insertRelationship(Relationship(follower, followed, Relationship.RelationshipType.aliasOf(newRelationship)))
    }

    suspend fun deleteRelationship(follower: String, followed: String) {
        val currentRelationship = getRelationshipsBetweenUsers(follower, followed)
        // If the current relationship is already null, there is nothing to remove.
        if (currentRelationship != null) {
            relationshipDAO.deleteRelationships(Relationship(follower, followed, currentRelationship.type))
        }
    }
}