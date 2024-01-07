package com.michael.template.feature.contacts.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.time.ZonedDateTime

class ContactPhoneConverter {

    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType: Type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromTimestamp(value: String?): ZonedDateTime? {
        return value?.let { ZonedDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: ZonedDateTime?): String? {
        return date?.toString()
    }
}
