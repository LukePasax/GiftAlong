package com.giacomosirri.myapplication.data.entity

import android.media.Image
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URL

@Entity(tableName = "Items")
data class Item(
    val name: String,
    val description: String,
    val url: URL,
    val image: Image,
    val bought: Boolean,
    @ColumnInfo("price_lower")
    val priceLowerBound: Double,
    @ColumnInfo("price_upper")
    val priceUpperBound: Double,
    @ColumnInfo("reserved_by_user")
    val reservedBy: String,
    @ColumnInfo("listed_by_user")
    val listedBy: String
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}