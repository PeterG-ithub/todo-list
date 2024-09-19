package com.example.todo_list_v1.ui.completed_task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_list_v1.data.completed_task.CompletedTask
import com.example.todo_list_v1.data.completed_task.CompletedTaskRepository
import com.example.todo_list_v1.data.task.Task
import com.example.todo_list_v1.data.task.TasksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CompletedTaskViewModel(
    private val completedTaskRepository: CompletedTaskRepository,
    private val tasksRepository: TasksRepository // Only if you need to restore tasks to the active list
) : ViewModel() {
    private val _completedTasks = MutableStateFlow<List<CompletedTask>>(emptyList())
    val completedTasks: StateFlow<List<CompletedTask>> = _completedTasks

    fun loadCompletedTasksByCategory(categoryId: Int?) {
        viewModelScope.launch {
            completedTaskRepository.getAllCompletedTasksStream()
                .collect { allTasks ->
                    _completedTasks.value = if (categoryId == null) {
                        allTasks.sortedByDescending { it.completedAt }
                    } else {
                        allTasks
                            .filter { it.taskCategoryId == categoryId } // Use taskCategoryId for filtering
                            .sortedByDescending { it.completedAt }
                    }
                }
        }
    }

    fun getGroupedTasks(): Map<String, List<CompletedTask>> {
        return _completedTasks.value.groupBy {
            val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            dateFormat.format(Date(it.completedAt))
        }
    }

    fun restoreTask(completedTask: CompletedTask) {

    }

    fun deleteCompletedTask(completedTask: CompletedTask) {
        viewModelScope.launch {
            completedTaskRepository.deleteCompletedTask(completedTask)
        }
    }
}
