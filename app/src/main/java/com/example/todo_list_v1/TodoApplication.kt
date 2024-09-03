package com.example.todo_list_v1

import android.app.Application
import com.example.todo_list_v1.data.AppContainer
import com.example.todo_list_v1.data.AppDataContainer

class TodoApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}