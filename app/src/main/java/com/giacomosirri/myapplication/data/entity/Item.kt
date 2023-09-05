package com.giacomosirri.myapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Items")
data class Item(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val bought: Boolean,
    val name: String,
    val description: String?,
    val url: String?,
    @ColumnInfo("image")
    val imageUri: String?,
    @ColumnInfo("price_lower")
    val priceLowerBound: Int?,
    @ColumnInfo("price_upper")
    val priceUpperBound: Int?,
    @ColumnInfo("reserved_by")
    val reservedBy: String?,
    @ColumnInfo("listed_by")
    val listedBy: String
)