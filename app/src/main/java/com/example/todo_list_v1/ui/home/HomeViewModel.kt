package com.example.todo_list_v1.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_list_v1.data.category.Category
import com.example.todo_list_v1.data.category.CategoryRepository
import com.example.todo_list_v1.data.completed_task.CompletedTask
import com.example.todo_list_v1.data.completed_task.CompletedTaskRepository
import com.example.todo_list_v1.data.task.Task
import com.example.todo_list_v1.data.task.TasksRepository
import com.example.todo_list_v1.ui.task.entry.calculateNextOccurrence
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

class HomeViewModel(
    private val tasksRepository: TasksRepository,
    private val categoryRepository: CategoryRepository,
    private val completedTaskRepository: CompletedTaskRepository
) : ViewModel() {

    private val _selectedCategoryId = MutableStateFlow<Int?>(null) // Null means "All"

    // StateFlow for the currently selected category ID
    val selectedCategoryId: StateFlow<Int?> = _selectedCategoryId

    // Task list for the currently selected category or all tasks
    @OptIn(ExperimentalCoroutinesApi::class)
    val filteredTasks: StateFlow<List<Task>> = _selectedCategoryId.flatMapLatest { categoryId ->
        if (categoryId == null) {
            tasksRepository.getAllTasksStream()
        } else {
            tasksRepository.getTasksByCategoryStream(categoryId)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), emptyList())

    val homeUiState: StateFlow<HomeUiState> = combine(
        filteredTasks,
        categoryRepository.getAllCategoriesStream()
    ) { tasks, categories ->
        HomeUiState(taskList = tasks, categoryList = categories)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )

    // Update the selected category and trigger a new task fetch
    fun selectCategory(categoryId: Int?) {
        _selectedCategoryId.value = categoryId
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