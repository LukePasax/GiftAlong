package com.giacomosirri.myapplication.data.dao

import androidx.room.*
import com.giacomosirri.myapplication.data.entity.User

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): User?

    @Delete(entity = User::class)
    suspend fun deleteUser(username: String)
}