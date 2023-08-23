package com.giacomosirri.myapplication.data.dao

import androidx.room.Query

interface ItemDAO {
    @Query("SELECT * FROM items WHERE id = :id")
    fun getItemSpecifics(id: String)
}