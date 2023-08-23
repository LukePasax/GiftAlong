package com.giacomosirri.myapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "relationships")
data class Relationship(
    val follower: String,
    val followed: String,
    val type: RelationshipType
) {
    enum class RelationshipType {
        FRIEND,
        FAMILY,
        PARTNER,
        COLLEAGUE
    }

    @PrimaryKey(autoGenerate = true)
    val id : Int = 0
}