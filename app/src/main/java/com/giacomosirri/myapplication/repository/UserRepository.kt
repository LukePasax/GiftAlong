package com.giacomosirri.myapplication.repository

import androidx.annotation.WorkerThread
import com.giacomosirri.myapplication.data.dao.UserDAO
import com.giacomosirri.myapplication.data.entity.User
import java.util.*

class UserRepository(private val userDAO: UserDAO) {

    @WorkerThread
    suspend fun insertUser(username: String, password: String, name: String, surname: String, birthday: Date) {
        val user = User(username, password, name, surname, birthday, Date())
        userDAO.insert(user)
    }

    @WorkerThread
    suspend fun getUser(username: String, password: String): Boolean = userDAO.getUser(username, password) != null
}