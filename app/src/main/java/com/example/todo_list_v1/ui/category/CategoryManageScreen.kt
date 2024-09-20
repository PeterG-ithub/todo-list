package com.example.todo_list_v1.ui.category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_list_v1.R
import com.example.todo_list_v1.TodoTopAppBar
import com.example.todo_list_v1.data.category.Category
import com.example.todo_list_v1.ui.AppViewModelProvider
import com.example.todo_list_v1.ui.category.list.CategoryList
import com.example.todo_list_v1.ui.completed_task.CompletedTaskViewModel
import com.example.todo_list_v1.ui.home.CategoryRepositoryMock
import com.example.todo_list_v1.ui.navigation.NavigationDestination
import com.example.todo_list_v1.ui.theme.Todolistv1Theme

object CategoryManageDestination : NavigationDestination {
    override val route = "category_manage"
    override val titleRes = R.string.manage_categories
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManageScreen(
    canNavigateBack: Boolean = true,
    onNavigateUp: () -> Unit,
    viewModel: CategoryViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    // Trigger fetching of categories on first render
    LaunchedEffect(Unit) {
        viewModel.fetchAllCategories()
    }

    Scaffold(
        topBar = {
            TodoTopAppBar(
                title = stringResource(id = R.string.manage_categories),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .fillMaxSize()
        ) {
            // Observe categories from the ViewModel
            val categories by viewModel.allCategories.collectAsState()

            // Check if the list is not empty and display it
            if (categories.isNotEmpty()) {
                CategoryList(
                    categories = categories,
                    onCategoryClick = { /* Handle click */ },
                    onDeleteClick = { category ->
                        viewModel.deleteCategory(category)
                    },
                    onVisibilityClick = { /* Handle visibility */ }
                )
            } else {
                // Show a message when there are no categories
                Text(text = "No categories available")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryManageScreenPreview(
) {
    Todolistv1Theme {
        CategoryManageScreen(
            onNavigateUp = { },
            viewModel = CategoryViewModel(
                categoryRepository = CategoryRepositoryMock(listOf(
                    Category(id = 1, name = "Work", description = "Work tasks", color = "#FF5733"),
                    Category(id = 2, name = "Personal", description = "Personal tasks", color = "#33FF57")
                )),
            )
        )
    }
}