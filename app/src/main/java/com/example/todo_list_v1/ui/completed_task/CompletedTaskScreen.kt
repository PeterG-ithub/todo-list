package com.example.todo_list_v1.ui.completed_task

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import com.example.todo_list_v1.R
import com.example.todo_list_v1.TodoTopAppBar
import com.example.todo_list_v1.ui.navigation.NavigationDestination

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
    }

}