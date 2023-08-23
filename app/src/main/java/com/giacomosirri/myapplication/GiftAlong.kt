package com.giacomosirri.myapplication

import android.app.Application
import com.giacomosirri.myapplication.data.AppDatabase

class GiftAlong : Application() {
    // lazy means that the database and the repository are only created when they are needed.
    private val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { ItemRepository(database.itemDao()) }
}