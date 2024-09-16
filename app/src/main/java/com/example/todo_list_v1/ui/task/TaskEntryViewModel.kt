package com.example.todo_list_v1.ui.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_list_v1.data.category.Category
import com.example.todo_list_v1.data.category.CategoryRepository
import com.example.todo_list_v1.data.task.Task
import com.example.todo_list_v1.data.task.TasksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskEntryViewModel(
    private val tasksRepository: TasksRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    /**
     * Holds current task UI state
     */
    var taskUiState by mutableStateOf(TaskUiState())
        private set

    /**
     * Holds the list of categories
     */
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    init {
        // Load categories when the ViewModel is initialized
        viewModelScope.launch {
            categoryRepository.getAllCategoriesStream().collect { categoryList ->
                _categories.value = categoryList
            }
        }
    }

    /**
     * Updates the [taskUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(taskDetails: TaskDetails) {
        taskUiState =
            TaskUiState(taskDetails = taskDetails, isEntryValid = validateInput(taskDetails))
    }

    /**
     * Inserts a [Task] in the data source
     */
    suspend fun saveTask() {
        if (validateInput()) {
            tasksRepository.insertTask(taskUiState.taskDetails.toTask())
        }
    }

    private fun validateInput(uiState: TaskDetails = taskUiState.taskDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() // Adjust validation as needed
        }
    }
}

/**
 * Represents UI State for a Task.
 */
data class TaskUiState(
    val taskDetails: TaskDetails = TaskDetails(),
    val isEntryValid: Boolean = false
)

data class TaskDetails(
    val id: Int = 0,
    val name: String = "",
    val description: String? = null,
    val isCompleted: Boolean = false,
    val dueDate: Long? = null,
    val reminderTime: Long? = null,
    val repeatFrequency: String? = null, // e.g., "daily", "weekly", "monthly", "yearly"
    val repeatInterval: Int? = null, // e.g., 2 (for "every 2 days", "every 3 weeks", etc.)
    val repeatEndsAt: Long? = null, // Timestamp when the repetition ends
    val repeatOnDays: List<Int>? = null, // List of days for weekly custom repeat (0 = Sunday, 6 = Saturday)
    val nextOccurrence: Long? = null, // Timestamp for the next occurrence (calculated)
    val priority: Int = 0,
    val tags: String? = null,
    val createdDate: Long = System.currentTimeMillis(),
    val lastUpdated: Long = System.currentTimeMillis(),
    val isTracking: Boolean = false,
    val trackingStartTime: Long? = null,
    val totalTrackingTime: Long = 0,
    val expectedStartTime: Long? = null,
    val expectedStopTime: Long? = null,
    val categoryId: Int? = null
)

fun TaskDetails.toTask(): Task = Task(
    id = id,
    name = name,
    description = description,
    isCompleted = isCompleted,
    dueDate = dueDate,
    reminderTime = reminderTime,
    repeatFrequency = repeatFrequency,
    repeatInterval = repeatInterval,
    repeatEndsAt = repeatEndsAt,
    repeatOnDays = repeatOnDays,
    nextOccurrence = nextOccurrence,
    priority = priority,
    tags = tags,
    createdDate = createdDate,
    lastUpdated = lastUpdated,
    isTracking = isTracking,
    trackingStartTime = trackingStartTime,
    totalTrackingTime = totalTrackingTime,
    expectedStartTime = expectedStartTime,
    expectedStopTime = expectedStopTime,
    categoryId = categoryId
)

fun Task.toTaskUiState(isEntryValid: Boolean = false): TaskUiState = TaskUiState(
    taskDetails = this.toTaskDetails(),
    isEntryValid = isEntryValid
)

fun Task.toTaskDetails(): TaskDetails = TaskDetails(
    id = id,
    name = name,
    description = description,
    isCompleted = isCompleted,
    dueDate = dueDate,
    reminderTime = reminderTime,
    repeatFrequency = repeatFrequency,
    repeatInterval = repeatInterval,
    repeatEndsAt = repeatEndsAt,
    repeatOnDays = repeatOnDays,
    nextOccurrence = nextOccurrence,
    priority = priority,
    tags = tags,
    createdDate = createdDate,
    lastUpdated = lastUpdated,
    isTracking = isTracking,
    trackingStartTime = trackingStartTime,
    totalTrackingTime = totalTrackingTime,
    expectedStartTime = expectedStartTime,
    expectedStopTime = expectedStopTime,
    categoryId = categoryId
)