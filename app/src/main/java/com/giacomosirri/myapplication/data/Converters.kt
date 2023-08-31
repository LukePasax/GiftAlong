package com.giacomosirri.myapplication.data

import androidx.room.TypeConverter
import java.text.DateFormat
import java.util.Date

class Converters {
    @TypeConverter
    fun fromDateToString(date: Date): String {
        return DateFormat.getDateInstance().format(date)
    }

    @TypeConverter
    fun fromStringToDate(date: String): Date {
        return DateFormat.getDateInstance().parse(date)!!
    }
}