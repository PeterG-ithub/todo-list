package com.example.todo_list_v1.data

import android.content.Context
import com.example.todo_list_v1.data.category.CategoryRepository
import com.example.todo_list_v1.data.category.OfflineCategoryRepository
import com.example.todo_list_v1.data.task.OfflineTasksRepository
import com.example.todo_list_v1.data.task.TasksRepository

interface AppContainer {
    val tasksRepository: TasksRepository
    val categoryRepository: CategoryRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val tasksRepository: TasksRepository by lazy {
        OfflineTasksRepository(AppDatabase.getDatabase(context).taskDao())
    }

    override val categoryRepository: CategoryRepository by lazy {
        OfflineCategoryRepository(AppDatabase.getDatabase(context).categoryDao())
    }
}