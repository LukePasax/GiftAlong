package com.giacomosirri.myapplication.repository

import androidx.annotation.WorkerThread
import com.giacomosirri.myapplication.R
import com.giacomosirri.myapplication.data.dao.ItemDAO
import com.giacomosirri.myapplication.data.entity.Item
import kotlinx.coroutines.flow.Flow

class ItemRepository(private val itemDAO: ItemDAO) {

    @WorkerThread
    suspend fun getItemFromId(id: Int) : Item = itemDAO.getItemFromId(id)

    @WorkerThread
    fun getItemsOfUser(username: String) : Flow<List<Item>> = itemDAO.getItemsOfUser(username)

    @WorkerThread
    suspend fun insertItem(
        name: String,
        description: String? = null,
        url: String? = null,
        image: Int,
        priceL: Double? = null,
        priceU: Double? = null,
        listedBy: String
    ) {
        itemDAO.insert(Item(bought = false, name, description, url, image, priceL, priceU, reservedBy = null, listedBy))
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
        priceL: Double? = null,
        priceU: Double? = null,
        reservedBy: String? = null,
    ) {
        /* TODO needs fixing */
        val oldItem = itemDAO.getItemFromId(id)
        val item = Item(
            bought ?: oldItem.bought,
            name ?: oldItem.name,
            description ?: oldItem.description,
            url ?: oldItem.url,
            image ?: oldItem.imageId,
            priceL ?: oldItem.priceLowerBound,
            priceU ?: oldItem.priceUpperBound,
            reservedBy ?: oldItem.reservedBy,
            oldItem.listedBy
        )
        itemDAO.updateItem(item)
    }
}