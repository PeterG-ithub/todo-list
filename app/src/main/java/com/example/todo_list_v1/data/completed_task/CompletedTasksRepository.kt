package com.example.todo_list_v1.data.completed_task

import kotlinx.coroutines.flow.Flow

interface CompletedTaskRepository {

    // Get a stream of all completed tasks
    fun getAllCompletedTasksStream(): Flow<List<CompletedTask>>

    // Get a stream of completed tasks by task ID
    fun getCompletedTasksByTaskIdStream(taskId: Int): Flow<List<CompletedTask>>

    // Get a stream of a specific completed task by its ID
    fun getCompletedTaskStream(id: Int): Flow<CompletedTask?>

    // Insert a new completed task
    suspend fun insertCompletedTask(completedTask: CompletedTask)

    // Update an existing completed task
    suspend fun updateCompletedTask(completedTask: CompletedTask)

    // Delete a specific completed task
    suspend fun deleteCompletedTask(completedTask: CompletedTask)

    // Delete all completed tasks
    suspend fun deleteAllCompletedTasks()
}
