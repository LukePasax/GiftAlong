package com.giacomosirri.myapplication.data.dao

import androidx.room.*
import com.giacomosirri.myapplication.data.entity.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDAO {
    @Update
    suspend fun updateItem(item: Item)

    @Query("SELECT * FROM items WHERE listed_by = :username")
    fun getItemsOfUser(username: String): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun getItemFromId(id: Int): Item

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)
}