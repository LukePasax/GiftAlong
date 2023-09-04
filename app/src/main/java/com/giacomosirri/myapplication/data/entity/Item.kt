package com.giacomosirri.myapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Items")
data class Item(
    val bought: Boolean,
    val name: String,
    val description: String?,
    val url: String?,
    @ColumnInfo("image")
    val imageId: Int?,
    @ColumnInfo("price_lower")
    val priceLowerBound: Double?,
    @ColumnInfo("price_upper")
    val priceUpperBound: Double?,
    @ColumnInfo("reserved_by")
    val reservedBy: String?,
    @ColumnInfo("listed_by")
    val listedBy: String
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}