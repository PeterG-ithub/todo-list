package com.example.todo_list_v1.ui.completed_task

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_list_v1.R
import com.example.todo_list_v1.TodoTopAppBar
import com.example.todo_list_v1.data.completed_task.CompletedTask
import com.example.todo_list_v1.ui.AppViewModelProvider
import com.example.todo_list_v1.ui.home.CompletedTaskRepositoryMock
import com.example.todo_list_v1.ui.home.TasksRepositoryMock
import com.example.todo_list_v1.ui.navigation.NavigationDestination
import com.example.todo_list_v1.ui.theme.Todolistv1Theme

object CompletedTaskDestination : NavigationDestination {
    override val route = "completed_task"
    override val titleRes = R.string.completed_task_title
    const val categoryIdArg = "categoryId"
    val routeWithArgs = "${CompletedTaskDestination.route}/{$categoryIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompletedTaskScreen(
    categoryId: Int?,
    canNavigateBack: Boolean = true,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    viewModel: CompletedTaskViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(categoryId) {
        viewModel.loadCompletedTasksByCategory(categoryId)
    }

    Scaffold(
        topBar = {
            TodoTopAppBar(
                title = stringResource(id = R.string.completed_task_title),
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
            if (uiState.completedTasks.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.no_completed_tasks),
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    uiState.groupedTasks.forEach { (date, tasks) ->
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = date,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                        }
                        items(tasks, key = { it.id }) { completedTask ->
                            CompletedTaskItem(
                                completedTask = completedTask,
                                onDelete = { task -> viewModel.deleteCompletedTask(task) },
                                onUndo = { task -> viewModel.restoreTask(task) }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CompletedTaskScreenPreview() {
    Todolistv1Theme {
        CompletedTaskScreen(
            categoryId = null,
            navigateBack = { /* Handle navigation back */ },
            onNavigateUp = { /* Handle navigate up */ },
            viewModel = CompletedTaskViewModel(
                completedTaskRepository = CompletedTaskRepositoryMock(
                    listOf(
                        CompletedTask(
                            id = 1,
                            taskId = 1,
                            taskName = "Completed Task 1",
                            taskDescription = "This is the first completed task.",
                            taskCategoryId = 1, // Assuming category ID is an Int
                            taskDueDate = null,
                            completedAt = System.currentTimeMillis() - 3600000 // Example timestamp
                        ),
                        CompletedTask(
                            id = 2,
                            taskId = 2,
                            taskName = "Completed Task 2",
                            taskDescription = "This is the second completed task.",
                            taskCategoryId = 2, // Assuming category ID is an Int
                            taskDueDate = null,
                            completedAt = System.currentTimeMillis() - 7200000 // Example timestamp
                        )
                    )
                ),
                tasksRepository = TasksRepositoryMock()
            )
        )
    }
}
