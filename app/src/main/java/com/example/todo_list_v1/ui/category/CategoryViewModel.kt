package com.example.todo_list_v1.ui.category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_list_v1.data.category.Category
import com.example.todo_list_v1.data.category.CategoryRepository
import kotlinx.coroutines.launch

class CategoryViewModel(private val categoryRepository: CategoryRepository) : ViewModel() {

    var categoryUiState by mutableStateOf(CategoryUiState())
        private set


    // State to hold all categories
    var allCategories by mutableStateOf<List<Category>>(emptyList())
        private set

    // Method to fetch all categories
    fun fetchAllCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategoriesStream().collect { categories ->
                allCategories = categories
            }
        }
    }

    fun updateUiState(categoryDetails: CategoryDetails) {
        categoryUiState =
            CategoryUiState(categoryDetails = categoryDetails, isEntryValid = validateInput(categoryDetails))
    }

    suspend fun saveCategory() {
        if (validateInput()) {
            categoryRepository.insertCategory(categoryUiState.categoryDetails.toCategory())
        }
    }

    suspend fun updateCategory() {
        if (validateInput()) {
            categoryRepository.updateCategory(categoryUiState.categoryDetails.toCategory())
        }
    }

    suspend fun deleteCategory() {
        categoryUiState.categoryDetails.id?.let {
            categoryRepository.deleteCategory(categoryUiState.categoryDetails.toCategory())
        }
    }

    suspend fun archiveCategory() {
        categoryUiState.categoryDetails.id?.let {
            categoryRepository.archiveCategory(it)
        }
    }

    private fun validateInput(uiState: CategoryDetails = categoryUiState.categoryDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() // Adjust validation as needed
        }
    }
}