package com.giacomosirri.myapplication.data.entity

import androidx.room.Entity

@Entity(tableName = "relationships", primaryKeys = ["follower", "followed", "type"])
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
}