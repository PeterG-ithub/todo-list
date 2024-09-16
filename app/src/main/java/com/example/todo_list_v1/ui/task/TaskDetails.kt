package com.example.todo_list_v1.ui.task

import com.example.todo_list_v1.data.task.Task

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