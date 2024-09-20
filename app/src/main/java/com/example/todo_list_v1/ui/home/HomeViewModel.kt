package com.example.todo_list_v1.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_list_v1.data.category.Category
import com.example.todo_list_v1.data.category.CategoryRepository
import com.example.todo_list_v1.data.completed_task.CompletedTask
import com.example.todo_list_v1.data.completed_task.CompletedTaskRepository
import com.example.todo_list_v1.data.task.Task
import com.example.todo_list_v1.data.task.TasksRepository
import com.example.todo_list_v1.util.DateUtils.calculateNextOccurrence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class HomeViewModel(
    private val tasksRepository: TasksRepository,
    private val categoryRepository: CategoryRepository,
    private val completedTaskRepository: CompletedTaskRepository
) : ViewModel() {

    private val _selectedCategoryId = MutableStateFlow<Int?>(null) // Null means "All"
    private val _showTodayTasks = MutableStateFlow(false) // New StateFlow for "Today" filter

    // StateFlow for the currently selected category ID
    val selectedCategoryId: StateFlow<Int?> = _selectedCategoryId
    val showTodayTasks: StateFlow<Boolean> = _showTodayTasks

    // Task list for the currently selected category or all tasks, or tasks due today
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalCoroutinesApi::class)
    val filteredTasks: StateFlow<List<Task>> = combine(
        _selectedCategoryId,
        _showTodayTasks,
        tasksRepository.getAllTasksStream()
    ) { categoryId, showTodayTasks, allTasks ->
        allTasks.filter { task ->
            when {
                showTodayTasks -> {
                    task.dueDate?.let { dueDateMillis ->
                        // Convert milliseconds to LocalDate
                        val dueDate = Instant.ofEpochMilli(dueDateMillis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        // Check if dueDate is today
                        dueDate == LocalDate.now()
                    } ?: false
                }
                categoryId == null -> true
                else -> task.categoryId == categoryId
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), emptyList())

    val homeUiState: StateFlow<HomeUiState> = combine(
        filteredTasks,
        categoryRepository.getAllCategoriesStream()
    ) { tasks, categories ->
        val visibleCategories = categories.filter { it.isVisible }.sortedBy { it.order }
        HomeUiState(taskList = tasks, categoryList = visibleCategories)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )

    // Update the selected category and clear "Today" filter
    fun selectCategory(categoryId: Int?) {
        _selectedCategoryId.value = categoryId
        _showTodayTasks.value = false // Clear "Today" filter when category is selected
    }

    // Set the filter to show "Today" tasks and clear category filter
    fun showTodayTasks() {
        _showTodayTasks.value = true
        _selectedCategoryId.value = null // Clear selected category when showing today’s tasks
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            tasksRepository.deleteTask(task)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(category)
        }
    }

    fun updateTaskCompletion(task: Task, isCompleted: Boolean) {
        viewModelScope.launch {
            val updatedTask = task.copy(isCompleted = isCompleted)
            tasksRepository.updateTask(updatedTask)

            if (isCompleted) {
                val categoryName: String? = task.categoryId?.let { categoryId ->
                    categoryRepository.getCategoryStream(categoryId)
                        .firstOrNull()?.name
                }
                val categoryId: Int? = task.categoryId

                val completedTask = CompletedTask(
                    taskId = task.id,
                    taskName = task.name,
                    taskDescription = task.description,
                    taskDueDate = task.dueDate,
                    completedAt = System.currentTimeMillis(),
                    taskCategoryId = categoryId,
                    taskCategory = categoryName
                )
                completedTaskRepository.insertCompletedTask(completedTask)
            }

            if (task.nextOccurrence != null) {
                // Recalculate next occurrence for repeating tasks
                val nextOccurrence = calculateNextOccurrence(
                    selectedRepeatOption = task.repeatFrequency,
                    repeatInterval = task.repeatInterval,
                    repeatEndsAt = task.repeatEndsAt,
                    repeatOnDays = task.repeatOnDays,
                    currentDueDate = task.dueDate
                )

                // Update task with new occurrence and mark it as not completed
                val updatedRecurringTask = task.copy(
                    isCompleted = false, // Uncheck the task
                    nextOccurrence = nextOccurrence,
                    dueDate = nextOccurrence
                )
                tasksRepository.updateTask(updatedRecurringTask)
            } else {
                tasksRepository.deleteTask(updatedTask)
            }
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI State for HomeScreen
 */
data class HomeUiState(
    val taskList: List<Task> = listOf(),
    val categoryList: List<Category> = listOf() // Add category list
)