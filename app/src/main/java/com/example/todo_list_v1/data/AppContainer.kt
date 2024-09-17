package com.example.todo_list_v1.data

import android.content.Context
import com.example.todo_list_v1.data.category.CategoryRepository
import com.example.todo_list_v1.data.category.OfflineCategoryRepository
import com.example.todo_list_v1.data.completed_task.CompletedTaskRepository
import com.example.todo_list_v1.data.completed_task.OfflineCompletedTaskRepository
import com.example.todo_list_v1.data.task.OfflineTasksRepository
import com.example.todo_list_v1.data.task.TasksRepository

interface AppContainer {
    val tasksRepository: TasksRepository
    val categoryRepository: CategoryRepository
    val completedTaskRepository: CompletedTaskRepository  // New property for CompletedTaskRepository
}


class AppDataContainer(private val context: Context) : AppContainer {
    override val tasksRepository: TasksRepository by lazy {
        OfflineTasksRepository(AppDatabase.getDatabase(context).taskDao())
    }

    override val categoryRepository: CategoryRepository by lazy {
        OfflineCategoryRepository(AppDatabase.getDatabase(context).categoryDao())
    }

    override val completedTaskRepository: CompletedTaskRepository by lazy {
        OfflineCompletedTaskRepository(AppDatabase.getDatabase(context).completedTaskDao())
    }
}
