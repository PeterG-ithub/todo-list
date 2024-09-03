package com.example.todo_list_v1.data

import android.content.Context

interface AppContainer {
    val tasksRepository: TasksRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val tasksRepository: TasksRepository by lazy {
        OfflineTasksRepository(AppDatabase.getDatabase(context).taskDao())
    }
}