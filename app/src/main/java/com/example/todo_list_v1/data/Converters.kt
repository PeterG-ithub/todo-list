package com.example.todo_list_v1.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromIntListToString(list: List<Int>?): String? {
        return list?.joinToString(",")
    }

    @TypeConverter
    fun fromStringToIntList(value: String?): List<Int>? {
        return if (!value.isNullOrBlank()) {
            value.split(",").mapNotNull {
                it.trim().toIntOrNull() // Safely convert to Int, ignore if it's not a valid integer
            }
        } else {
            emptyList() // Return an empty list if the input is null or blank
        }
    }
}