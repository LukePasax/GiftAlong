package com.giacomosirri.myapplication.data

import androidx.room.TypeConverter
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class Converters {
    @TypeConverter
    fun fromDateToString(date: Date): String {
        return ""  //TODO
    }

    @TypeConverter
    fun fromStringToDate(date: String): Date {
        return Date()  //TODO
    }

    @TypeConverter
    fun fromURLtoString(url: URL): String = url.toString()

    @TypeConverter
    fun fromStringToURL(url: String): URL? {
        try {
            return URL(url)
        } catch (e: MalformedURLException) {
            e.printStackTrace();
        }
        return null
    }
}