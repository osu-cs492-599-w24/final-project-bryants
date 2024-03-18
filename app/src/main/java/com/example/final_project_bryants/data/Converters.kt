package com.example.final_project_bryants.data

import androidx.room.TypeConverter
class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): java.util.Date? {
        return value?.let { java.util.Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(data: java.util.Date?): Long? = data?.time
}