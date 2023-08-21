package com.giacomosirri.myapplication.data

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "events")
class Event {
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id : Int = 0

    @ColumnInfo(name = "name")
    val name : String = ""

    @ColumnInfo(name = "description")
    val description : String = ""

    @ColumnInfo(name = "date")
    val date : Date = Date()

    @ColumnInfo(name = "location")
    val location : Location = Location("")

    @ColumnInfo(name = "organizer")
    val organizer : String = ""

    @ColumnInfo(name = "participants")
    val participants : List<String> = listOf()

    @ColumnInfo(name = "dress_code")
    val dressCode : String = ""
}