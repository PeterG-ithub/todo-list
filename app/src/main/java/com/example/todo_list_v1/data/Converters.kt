package com.example.todo_list_v1.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromIntListToString(list: List<Int>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun fromStringToIntList(value: String?): List<Int>? {
        return value?.split(",")?.map { it.toInt() }
    }
}