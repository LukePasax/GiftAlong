package com.giacomosirri.myapplication.data.entity

import androidx.room.Entity
import com.giacomosirri.myapplication.ui.AppContext

@Entity(tableName = "relationships", primaryKeys = ["follower", "followed"])
data class Relationship(
    val follower: String,
    val followed: String,
    val type: RelationshipType
) {
    enum class RelationshipType {
        Friend,
        Family,
        Partner,
        Colleague,
        None;

        companion object {
            fun aliasOf(type: String): RelationshipType {
                return when (type) {
                    "Amico", "Friend" -> Friend
                    "Famiglia", "Family" -> Family
                    "Partner" -> Partner
                    "Collega", "Colleague" -> Colleague
                    "Nessuno", "None" -> None
                    else -> throw IllegalArgumentException("Invalid relationship type")
                }
            }

            fun stringOf(type: RelationshipType): String {
                return when (type) {
                    Friend -> AppContext.getContext()?.getString(com.giacomosirri.myapplication.R.string.relationship_type_friend)!!
                    Family -> AppContext.getContext()?.getString(com.giacomosirri.myapplication.R.string.relationship_type_family)!!
                    Partner -> AppContext.getContext()?.getString(com.giacomosirri.myapplication.R.string.relationship_type_partner)!!
                    Colleague -> AppContext.getContext()?.getString(com.giacomosirri.myapplication.R.string.relationship_type_colleague)!!
                    None -> AppContext.getContext()?.getString(com.giacomosirri.myapplication.R.string.relationship_type_none)!!
                }
            }
        }
    }
}