package com.example.todo_list_v1.data.completed_task

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletedTaskDao {

    // Insert a new completed task
    @Insert
    suspend fun insertCompletedTask(completedTask: CompletedTask)

    // Update an existing completed task
    @Update
    suspend fun updateCompletedTask(completedTask: CompletedTask)

    // Delete a specific completed task
    @Delete
    suspend fun deleteCompletedTask(completedTask: CompletedTask)

    // Get all completed tasks as a Flow
    @Query("SELECT * FROM completed_tasks")
    fun getAllCompletedTasks(): Flow<List<CompletedTask>>

    // Get completed tasks by task ID as a Flow
    @Query("SELECT * FROM completed_tasks WHERE taskId = :taskId")
    fun getCompletedTasksByTaskId(taskId: Int): Flow<List<CompletedTask>>

    // Get a single completed task by ID as a Flow
    @Query("SELECT * FROM completed_tasks WHERE id = :id")
    fun getCompletedTaskById(id: Int): Flow<CompletedTask?>

    // Delete all completed tasks
    @Query("DELETE FROM completed_tasks")
    suspend fun deleteAllCompletedTasks()
}
