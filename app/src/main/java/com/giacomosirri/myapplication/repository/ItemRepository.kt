package com.giacomosirri.myapplication.repository

import androidx.annotation.WorkerThread
import com.giacomosirri.myapplication.data.dao.ItemDAO
import com.giacomosirri.myapplication.data.entity.Item
import kotlinx.coroutines.flow.Flow

class ItemRepository(private val itemDAO: ItemDAO) {

    @WorkerThread
    suspend fun getItemFromId(id: Int): Item = itemDAO.getItemFromId(id)

    @WorkerThread
    fun getItemsOfUser(username: String): Flow<List<Item>> = itemDAO.getItemsOfUser(username)

    @WorkerThread
    suspend fun insertItem(
        name: String,
        description: String? = null,
        url: String? = null,
        image: Int,
        priceL: Int? = null,
        priceU: Int? = null,
        listedBy: String
    ) {
        itemDAO.insertItem(Item(id = null, bought = false, name, description, url, image, priceL, priceU, reservedBy = null, listedBy))
    }

    @WorkerThread
    suspend fun deleteItem(id: Int) {
        itemDAO.deleteItem(itemDAO.getItemFromId(id))
    }

    @WorkerThread
    suspend fun updateItem(
        id: Int,
        bought: Boolean? = null,
        name: String? = null,
        description: String? = null,
        url: String? = null,
        image: Int? = null,
        priceL: Int? = null,
        priceU: Int? = null,
        reservedBy: String? = null,
    ) {
        val oldItem = itemDAO.getItemFromId(id)
        val item = Item(
            id = id,
            bought = bought ?: oldItem.bought,
            name = name ?: oldItem.name,
            description = description ?: oldItem.description,
            url = url ?: oldItem.url,
            imageId = image ?: oldItem.imageId,
            priceLowerBound = priceL ?: oldItem.priceLowerBound,
            priceUpperBound = priceU ?: oldItem.priceUpperBound,
            reservedBy = reservedBy ?: oldItem.reservedBy,
            oldItem.listedBy
        )
        itemDAO.updateItem(item)
    }
}