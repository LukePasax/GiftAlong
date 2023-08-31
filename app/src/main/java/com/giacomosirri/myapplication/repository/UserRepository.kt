package com.giacomosirri.myapplication.repository

import com.giacomosirri.myapplication.data.dao.UserDAO
import com.giacomosirri.myapplication.data.entity.User
import java.util.*

class UserRepository(private val userDAO: UserDAO) {
    suspend fun insertUser(username: String, password: String, name: String, surname: String, birthday: Date) {
        val user = User(password, name, surname, birthday, Date())
        userDAO.insert(user)
    }

    suspend fun getUser(username: String, password: String): Boolean = userDAO.getUser(username, password) != null
}