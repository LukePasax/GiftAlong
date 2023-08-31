package com.giacomosirri.myapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URL

@Entity(tableName = "Items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    val bought: Boolean,
    val name: String,
    val description: String?,
    val url: String?,
    @ColumnInfo("image")
    val imageURI: String?,  // URI of the pic that points to the remote database.
    @ColumnInfo("price_lower")
    val priceLowerBound: Double?,
    @ColumnInfo("price_upper")
    val priceUpperBound: Double?,
    @ColumnInfo("reserved_by")
    val reservedBy: String?,
    @ColumnInfo("listed_by")
    val listedBy: String
)