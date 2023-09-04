package com.giacomosirri.myapplication

import android.app.Application
import com.giacomosirri.myapplication.data.AppDatabase
import com.giacomosirri.myapplication.repository.EventRepository
import com.giacomosirri.myapplication.repository.ItemRepository
import com.giacomosirri.myapplication.repository.SettingsRepository
import com.giacomosirri.myapplication.repository.UserRepository
import com.giacomosirri.myapplication.ui.AppContext

class GiftAlong : Application() {
    // lazy means that the database and the repositories are only created when they are needed.
    private val database by lazy { AppDatabase.getDatabase(this) }
    val itemRepository by lazy { ItemRepository(database.itemDao()) }
    val userRepository by lazy { UserRepository(database.userDao(), database.relationshipDao()) }
    val eventRepository by lazy { EventRepository(database.eventDao()) }
    val settingsRepository by lazy { SettingsRepository(AppContext.getContext()!!) }
}