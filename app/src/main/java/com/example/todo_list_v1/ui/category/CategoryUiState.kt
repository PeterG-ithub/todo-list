package com.example.todo_list_v1.ui.category

data class CategoryUiState(
    val categoryDetails: CategoryDetails = CategoryDetails(),
    val isEntryValid: Boolean = false
)

data class CategoryDetails(
    val id: Int? = null,
    val name: String = "",
    val description: String? = null,
    val color: String? = null,
    val icon: String? = null,
    val createdDate: Long = System.currentTimeMillis(),
    val lastUpdated: Long = System.currentTimeMillis(),
    val isArchived: Boolean = false
)