package com.giacomosirri.myapplication.repository

import androidx.annotation.WorkerThread
import com.giacomosirri.myapplication.data.dao.UserDAO
import com.giacomosirri.myapplication.data.entity.User
import java.util.*

class UserRepository(private val userDAO: UserDAO) {
    @WorkerThread
    suspend fun insertUser(username: String, password: String, name: String, surname: String, image : Int, birthday: Date) {
        userDAO.insertUser(User(username, password, name, surname, birthday, image, Date()))
    }

    @WorkerThread
    suspend fun getUser(username: String, password: String): Boolean = userDAO.getUser(username, password) != null

    @WorkerThread
    suspend fun deleteUser(username: String) {
        userDAO.deleteUser(userDAO.getUser(username))
    }
}