package com.giacomosirri.myapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class RelationshipType {
    FRIEND,
    FAMILY,
    PARTNER,
    COLLEAGUE
}

@Entity(tableName = "relationships")
class Relationship {

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id : Int = 0

    @ColumnInfo(name = "follower")
    val follower : String = ""

    @ColumnInfo(name = "followed")
    val followed : String = ""

    @ColumnInfo(name = "type")
    val type : RelationshipType = RelationshipType.FRIEND

}