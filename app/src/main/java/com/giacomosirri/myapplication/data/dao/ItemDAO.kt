package com.giacomosirri.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.giacomosirri.myapplication.data.entity.Item

@Dao
interface ItemDAO {
    @Query("SELECT * FROM items WHERE id = :id")
    fun getItemSpecifics(id: String) : Item
}