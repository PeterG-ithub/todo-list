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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.todo_list_v1.R
import com.example.todo_list_v1.TodoTopAppBar
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
) {
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
            Text(
                "Hello world"
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CategoryManageScreenPreview(
) {
    Todolistv1Theme {
        CategoryManageScreen(
            onNavigateUp = { }
        )
    }
}