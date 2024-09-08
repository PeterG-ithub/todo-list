package com.example.todo_list_v1.ui.task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.todo_list_v1.data.task.TasksRepository

class TaskEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: TasksRepository
) : ViewModel() {

}