package com.giacomosirri.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.giacomosirri.myapplication.data.entity.Event
import com.giacomosirri.myapplication.data.entity.Item
import com.giacomosirri.myapplication.data.entity.User

@Database(entities = [User::class, Event::class, Item::class, Relationship::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context) : AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        name = "giftalong_database"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}