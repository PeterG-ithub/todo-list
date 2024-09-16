package com.example.todo_list_v1.data.completed_task

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.todo_list_v1.data.task.Task


@Entity(
    tableName = "completed_tasks",
    foreignKeys = [ForeignKey(
        entity = Task::class,
        parentColumns = ["id"],
        childColumns = ["taskId"],
        onDelete = ForeignKey.SET_NULL
    )]
)
data class CompletedTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val taskId: Int,
    val taskName: String,
    val taskDescription: String? = null,
    val taskCategory: String? = null,
    val taskDueDate: Long? = null,
    val completedAt: Long,
)