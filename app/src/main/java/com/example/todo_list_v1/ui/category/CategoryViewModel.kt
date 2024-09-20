package com.example.todo_list_v1.ui.category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_list_v1.data.category.Category
import com.example.todo_list_v1.data.category.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(private val categoryRepository: CategoryRepository) : ViewModel() {

    var categoryUiState by mutableStateOf(CategoryUiState())
        private set

    // State to hold all categories
    private val _allCategories = MutableStateFlow<List<Category>>(emptyList())
    val allCategories: StateFlow<List<Category>> = _allCategories.asStateFlow()

    // Method to fetch all categories
    fun fetchAllCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategoriesStream().collect { categories ->
                _allCategories.value = categories.sortedBy { it.order } // Sort by order
            }
        }
    }

    fun updateUiState(categoryDetails: CategoryDetails) {
        categoryUiState =
            CategoryUiState(categoryDetails = categoryDetails, isEntryValid = validateInput(categoryDetails))
    }

    suspend fun saveCategory() {
        if (validateInput()) {
            val existingCategories = _allCategories.value
            val newOrder = (existingCategories.maxOfOrNull { it.order } ?: 0) + 1
            val categoryDetails = categoryUiState.categoryDetails.toCategory().copy(order = newOrder)
            categoryRepository.insertCategory(categoryDetails)
        }
    }

    suspend fun updateCategory() {
        if (validateInput()) {
            categoryRepository.updateCategory(categoryUiState.categoryDetails.toCategory())
        }
    }

    fun toggleCategoryVisibility(category: Category) {
        val updatedCategory = category.copy(isVisible = !category.isVisible)
        viewModelScope.launch {
            categoryRepository.updateCategory(updatedCategory) // Assuming updateCategory updates the DB
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(category)
            reorderCategories(category)
        }
    }

    private suspend fun reorderCategories(deletedCategory: Category) {
        val existingCategories = _allCategories.value
            .filter { it.id != deletedCategory.id } // Exclude the deleted category
            .sortedBy { it.order } // Sort by current order

        existingCategories.forEachIndexed { index, category ->
            // Update the order of each category
            categoryRepository.updateCategory(category.copy(order = index + 1))
        }
    }

    suspend fun archiveCategory() {
        categoryUiState.categoryDetails.id?.let {
            categoryRepository.archiveCategory(it)
        }
    }

    private fun validateInput(uiState: CategoryDetails = categoryUiState.categoryDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && isNameUnique(name)
        }
    }

    private fun isNameUnique(name: String): Boolean {
        // Access the current value of allCategories to check uniqueness
        return allCategories.value.none { it.name.equals(name, ignoreCase = true) }
    }
}
