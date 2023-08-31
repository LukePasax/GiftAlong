package com.giacomosirri.myapplication.repository

import androidx.annotation.WorkerThread
import com.giacomosirri.myapplication.data.dao.ItemDAO
import com.giacomosirri.myapplication.data.entity.Item
import kotlinx.coroutines.flow.Flow

class ItemRepository(private val itemDAO: ItemDAO) {

    @WorkerThread
    fun getItemsOfUser(username: String) : Flow<List<Item>> = itemDAO.getItems(username)

    @WorkerThread
    suspend fun insertItem(name : String, description : String? = null, url : String? = null, image : String? = null, priceL : Double? = null, priceU : Double? = null, listedBy : String) {
        val item = Item(false, name, description, url, image, priceL, priceU, null, listedBy)
        itemDAO.insert(item)
    }

    @WorkerThread
    suspend fun deleteItem(id : Int) {
        itemDAO.delete(itemDAO.getItem(id))
    }


    @WorkerThread
    suspend fun updateItem(id : Int, bought : Boolean? = null, name : String? = null, description : String? = null, url : String? = null, image : String? = null, priceL : Double? = null, priceU : Double? = null, reservedBy : String? = null, listedBy : String? = null) {
        val oldItem = itemDAO.getItem(id)
        val item = Item(
            bought ?: oldItem.bought,
            name ?: oldItem.name,
            description ?: oldItem.description,
            url ?: oldItem.url,
            image ?: oldItem.imageURI,
            priceL ?: oldItem.priceLowerBound,
            priceU ?: oldItem.priceUpperBound,
            reservedBy ?: oldItem.reservedBy,
            oldItem.listedBy
        )
        itemDAO.update(item)
    }

}