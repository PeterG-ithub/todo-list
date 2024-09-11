package com.example.todo_list_v1.ui.task

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_list_v1.R
import com.example.todo_list_v1.TodoTopAppBar
import com.example.todo_list_v1.data.category.Category
import com.example.todo_list_v1.ui.AppViewModelProvider
import com.example.todo_list_v1.ui.category.CategoryEntryModal
import com.example.todo_list_v1.ui.navigation.NavigationDestination
import com.example.todo_list_v1.ui.task.entry.TaskEntryBody
import com.example.todo_list_v1.ui.theme.Todolistv1Theme
import kotlinx.coroutines.launch


object TaskEntryDestination : NavigationDestination {
    override val route = "task_entry"
    override val titleRes = R.string.add_task
    const val categoryIdArg = "taskId"
    val routeWithArgs = "${TaskEntryDestination.route}/{$categoryIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEntryScreen(
    categoryId: Int?,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: TaskEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val categories by viewModel.categories.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(categoryId) {
        val updatedTaskDetails = viewModel.taskUiState.taskDetails.copy(categoryId = categoryId)
        viewModel.updateUiState(updatedTaskDetails) // Update taskUiState with the new categoryId
    }

    Scaffold(
        topBar = {
            TodoTopAppBar(
                title = stringResource(R.string.task_entry_title),  // Update with the appropriate title resource
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        TaskEntryBody(
            taskUiState = viewModel.taskUiState,
            categories = categories,
            onTaskValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveTask()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun TaskEntryScreenPreview() {
    Todolistv1Theme {
        val categories = listOf<Category>()
        TaskEntryBody(
            taskUiState = TaskUiState(
                TaskDetails(
                    name = "Task name", description = "Task description"
                )
            ),
            onTaskValueChange = {},
            categories = categories,
            onSaveClick = {}
        )
    }
}