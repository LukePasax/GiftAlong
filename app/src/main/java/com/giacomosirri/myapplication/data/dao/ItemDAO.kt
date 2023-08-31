package com.giacomosirri.myapplication.data.dao

import androidx.room.*
import com.giacomosirri.myapplication.data.entity.Item

@Dao
interface ItemDAO {

    @Update
    suspend fun update(item: Item)

    @Query("SELECT * FROM items WHERE listed_by = :username")
    suspend fun getItems(username: String): List<Item>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getItem(id: Int): Item

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Delete
    suspend fun delete(item: Item)
}