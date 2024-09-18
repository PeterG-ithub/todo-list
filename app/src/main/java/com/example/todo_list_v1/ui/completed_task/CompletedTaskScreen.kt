package com.example.todo_list_v1.ui.completed_task

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_list_v1.R
import com.example.todo_list_v1.TodoTopAppBar
import com.example.todo_list_v1.data.completed_task.CompletedTask
import com.example.todo_list_v1.data.completed_task.CompletedTaskRepository
import com.example.todo_list_v1.ui.AppViewModelProvider
import com.example.todo_list_v1.ui.home.CompletedTaskRepositoryMock
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
    Scaffold(
        topBar = {
            TodoTopAppBar(
                title = stringResource(id = R.string.completed_task_title),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        Text(
            "Category Id: $categoryId",
            modifier = Modifier
                .padding(
                start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                top = innerPadding.calculateTopPadding(),
                end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
            )
        )
        Text(
            "Hello"
        )
    }

}

@Preview
@Composable
fun CompletedTaskScreenPreview() {
    Todolistv1Theme {
        CompletedTaskScreen(
            categoryId = null,
            navigateBack = { /*TODO*/ },
            onNavigateUp = { /*TODO*/ },
            viewModel = CompletedTaskViewModel(
                CompletedTaskRepositoryMock()
            )
        )
    }
}
