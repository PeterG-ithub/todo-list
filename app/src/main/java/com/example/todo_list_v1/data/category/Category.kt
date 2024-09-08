package com.example.todo_list_v1.data.category

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String, // Name of the category
    val description: String? = null, // A short description of the category
    val color: String? = null, // Hex color code for visual distinction
    val icon: String? = null, // An icon resource for the category (e.g., a file name or URI)
    val createdDate: Long = System.currentTimeMillis(), // Timestamp when category was created
    val lastUpdated: Long = System.currentTimeMillis(), // Timestamp when it was last updated
    val isArchived: Boolean = false // Archive flag to hide the category without deleting
)