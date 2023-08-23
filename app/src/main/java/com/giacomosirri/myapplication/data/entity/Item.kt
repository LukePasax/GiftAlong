package com.giacomosirri.myapplication.data.entity

import android.media.Image
import android.util.Range
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URL

@Entity(tableName = "Items")
data class Item(
    val name: String,
    val description: String,
    val url: URL,
    val image: Image,
    val range: Range<Double>,
    val reservedBy: User,
    val bought: Boolean,
    val ownedBy: User
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}