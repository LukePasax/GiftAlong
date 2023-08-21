package com.giacomosirri.myapplication.data

import android.media.Image
import android.util.Range
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Items")
class Item {
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id : Int = 0

    @ColumnInfo(name = "name")
    val name : String = ""

    @ColumnInfo(name = "description")
    val description : String = ""

    @ColumnInfo(name = "url")
    val url : String = ""

    @ColumnInfo(name = "image")
    val image : Image? = null

    @ColumnInfo(name = "price_range")
    val range : Range<Double> = Range(0.0,0.0)

    @ColumnInfo(name = "reserved_by")
    val reservedBy : String? = null
    
    @ColumnInfo(name = "is_bought")
    val isBought : Boolean = false

    @ColumnInfo(name = "user")
    val user : String = ""
}