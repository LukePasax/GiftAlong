package com.giacomosirri.myapplication

import android.app.Application
import com.giacomosirri.myapplication.data.AppDatabase
import com.giacomosirri.myapplication.repository.EventRepository
import com.giacomosirri.myapplication.repository.ItemRepository
import com.giacomosirri.myapplication.repository.UserRepository

class GiftAlong : Application() {
    // lazy means that the database and the repositories are only created when they are needed.
    private val database by lazy { AppDatabase.getDatabase(this) }
    val itemRepository by lazy { ItemRepository(database.itemDao()) }
    val userRepository by lazy { UserRepository(database.userDao()) }
    val eventRepository by lazy { EventRepository(database.eventDao()) }
}