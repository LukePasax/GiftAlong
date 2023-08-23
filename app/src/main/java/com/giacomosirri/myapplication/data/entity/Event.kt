package com.giacomosirri.myapplication.data.entity

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "events")
data class Event(
    val name: String,
    val date: Date,
    val location: Location,
    val organizer: String,
    val dressCode: String,
    @ColumnInfo("friends_allowed")
    val friendsAllowed: Boolean,
    @ColumnInfo("family_allowed")
    val familyAllowed: Boolean,
    @ColumnInfo("partner_allowed")
    val partnerAllowed: Boolean,
    @ColumnInfo("colleagues_allowed")
    val colleaguesAllowed: Boolean,
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}