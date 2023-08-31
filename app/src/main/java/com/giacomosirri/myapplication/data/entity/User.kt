package com.giacomosirri.myapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val username : String,
    val password: String,
    val name: String,
    val surname: String,
    val birthday: Date,
    @ColumnInfo("subscription_date")
    val subscriptionDate: Date,
)