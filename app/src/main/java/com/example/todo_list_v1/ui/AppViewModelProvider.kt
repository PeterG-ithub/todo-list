package com.example.todo_list_v1.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todo_list_v1.TodoApplication
import com.example.todo_list_v1.ui.category.CategoryViewModel
import com.example.todo_list_v1.ui.home.HomeViewModel
import com.example.todo_list_v1.ui.task.TaskEditViewModel
import com.example.todo_list_v1.ui.task.TaskEntryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                todoApplication().container.tasksRepository,
                todoApplication().container.categoryRepository,
                todoApplication().container.completedTaskRepository
            )
        }

        initializer {
            TaskEntryViewModel(
                todoApplication().container.tasksRepository,
                todoApplication().container.categoryRepository)
        }

        initializer {
            TaskEditViewModel(
                this.createSavedStateHandle(),
                todoApplication().container.tasksRepository,
                todoApplication().container.categoryRepository)
        }

        initializer {
            CategoryViewModel(
                todoApplication().container.categoryRepository
            )
        }
    }
}

fun CreationExtras.todoApplication(): TodoApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TodoApplication)
