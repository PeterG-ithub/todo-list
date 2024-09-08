package com.example.todo_list_v1.data.category

import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategoriesStream(): Flow<List<Category>>
    fun getCategoryStream(id: Int): Flow<Category?>
    suspend fun insertCategory(category: Category)
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
    suspend fun archiveCategory(categoryId: Int)
}
