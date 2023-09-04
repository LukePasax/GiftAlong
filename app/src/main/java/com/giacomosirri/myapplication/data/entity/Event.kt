package com.giacomosirri.myapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String,
    val date: Date,
    val location: String?,
    val organizer: String,
    val dressCode: String?,
    @ColumnInfo("friends_allowed")
    val friendsAllowed: Boolean,
    @ColumnInfo("family_allowed")
    val familyAllowed: Boolean,
    @ColumnInfo("partners_allowed")
    val partnersAllowed: Boolean,
    @ColumnInfo("colleagues_allowed")
    val colleaguesAllowed: Boolean,
)