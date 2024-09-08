package com.example.todo_list_v1.data

import android.content.Context
import com.example.todo_list_v1.data.task.OfflineTasksRepository
import com.example.todo_list_v1.data.task.TasksRepository

interface AppContainer {
    val tasksRepository: TasksRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val tasksRepository: TasksRepository by lazy {
        OfflineTasksRepository(AppDatabase.getDatabase(context).taskDao())
    }
}