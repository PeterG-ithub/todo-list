package com.example.todo_list_v1.ui.completed_task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_list_v1.data.completed_task.CompletedTask
import com.example.todo_list_v1.data.completed_task.CompletedTaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CompletedTaskViewModel(
    private val completedTaskRepository: CompletedTaskRepository
): ViewModel() {
    private val _completedTasks = MutableStateFlow<List<CompletedTask>>(emptyList())
    val completedTasks: StateFlow<List<CompletedTask>> = _completedTasks

    fun loadCompletedTasksByCategory(categoryId: Int?) {
        viewModelScope.launch {
            completedTaskRepository.getAllCompletedTasksStream()
                .collect { allTasks ->
                    // Filter by category ID and sort by completion date (most recent first)
                    _completedTasks.value = if (categoryId == null) {
                        allTasks.sortedByDescending { it.completedAt }
                    } else {
                        allTasks
                            .filter { it.taskCategory?.toIntOrNull() == categoryId }
                            .sortedByDescending { it.completedAt }
                    }
                }
        }
    }

    fun getGroupedTasks(): Map<String, List<CompletedTask>> {
        return _completedTasks.value.groupBy {
            // Format the completedAt timestamp into a date string
            val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            dateFormat.format(Date(it.completedAt))
        }
    }
}
