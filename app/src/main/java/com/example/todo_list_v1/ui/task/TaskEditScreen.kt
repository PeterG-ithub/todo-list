package com.example.todo_list_v1.ui.task

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_list_v1.R
import com.example.todo_list_v1.TodoTopAppBar
import com.example.todo_list_v1.ui.AppViewModelProvider
import com.example.todo_list_v1.ui.navigation.NavigationDestination

object TaskEditDestination : NavigationDestination {
    override val route = "task_edit"
    override val titleRes = R.string.edit_task_title
    const val taskIdArg = "taskId"
    val routeWithArgs = "$route/{$taskIdArg}"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    Scaffold(
        topBar = {
            TodoTopAppBar(
                title = stringResource(R.string.edit_task_title),  // Update with the appropriate title resource
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        }
    ) { innerpadding ->
        Text(
            text = "Edit Task Screen",
            modifier = modifier
                .padding(innerpadding)
        )
    }
}
