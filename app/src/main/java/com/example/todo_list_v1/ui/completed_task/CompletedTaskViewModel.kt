package com.example.todo_list_v1.ui.completed_task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_list_v1.data.completed_task.CompletedTask
import com.example.todo_list_v1.data.completed_task.CompletedTaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CompletedTaskViewModel(
    private val completedTaskRepository: CompletedTaskRepository
): ViewModel() {
    // StateFlow to hold the filtered list of completed tasks
    private val _completedTasks = MutableStateFlow<List<CompletedTask>>(emptyList())
    val completedTasks: StateFlow<List<CompletedTask>> = _completedTasks

    // Load tasks by category ID
    fun loadCompletedTasksByCategory(categoryId: Int?) {
        viewModelScope.launch {
            completedTaskRepository.getAllCompletedTasksStream()
                .collect { allTasks ->
                    // Filter by category ID, if provided
                    _completedTasks.value = if (categoryId == null) {
                        allTasks // Show all tasks if categoryId is null
                    } else {
                        allTasks.filter { it.taskCategory?.toIntOrNull() == categoryId }
                    }
                }
        }
    }
}
