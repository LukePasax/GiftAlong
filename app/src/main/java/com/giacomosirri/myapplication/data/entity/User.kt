package com.giacomosirri.myapplication.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "users")
class User {
    @PrimaryKey
    @ColumnInfo(name = "Username")
    val username : String = ""

    @ColumnInfo(name = "name")
    val name : String = ""

    @ColumnInfo(name = "surname")
    val surname : String = ""

    @ColumnInfo(name = "birthday")
    val birthday : Date = Date()

    @ColumnInfo(name = "sub_date")
    val subDate : Date = Date()

    @ColumnInfo(name = "password")
    val password : String = ""
}