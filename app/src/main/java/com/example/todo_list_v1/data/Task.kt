package com.example.todo_list_v1.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL // Optional: Set behavior when category is deleted
        )
    ],
    indices = [Index(value = ["categoryId"])] // For faster querying by category
)
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String? = null,
    val isCompleted: Boolean = false,
    val dueDate: Long? = null, // Timestamp (milliseconds since epoch)
    val reminderTime: Long? = null, // Timestamp
    val repeatFrequency: String? = null, // e.g., "daily", "weekly", "monthly"
    val priority: Int = 0, // e.g., 0 for low, 1 for medium, 2 for high
    val tags: String? = null, // Comma-separated tags or labels
    val createdDate: Long = System.currentTimeMillis(), // Timestamp
    val lastUpdated: Long = System.currentTimeMillis(), // Timestamp
    val isTracking: Boolean = false, // Whether the task is currently being tracked
    val trackingStartTime: Long? = null, // Timestamp when tracking started
    val totalTrackingTime: Long = 0, // Total time spent on the task
    val expectedStartTime: Long? = null, // Expected time when task is going to get started
    val expectedStopTime: Long? = null, // Expected time when task is over
    val categoryId: Int? = null // Foreign key to the Category table
)