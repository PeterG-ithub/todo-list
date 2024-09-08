package com.example.todo_list_v1.ui.category

import com.example.todo_list_v1.data.category.Category

fun CategoryDetails.toCategory(): Category = Category(
    id = id ?: 0,
    name = name,
    description = description,
    color = color,
    icon = icon,
    createdDate = createdDate,
    lastUpdated = lastUpdated,
    isArchived = isArchived
)

fun Category.toCategoryUiState(isEntryValid: Boolean = false): CategoryUiState = CategoryUiState(
    categoryDetails = this.toCategoryDetails(),
    isEntryValid = isEntryValid
)

fun Category.toCategoryDetails(): CategoryDetails = CategoryDetails(
    id = id,
    name = name,
    description = description,
    color = color,
    icon = icon,
    createdDate = createdDate,
    lastUpdated = lastUpdated,
    isArchived = isArchived
)