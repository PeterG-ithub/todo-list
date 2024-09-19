package com.example.todo_list_v1.ui.category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.todo_list_v1.R
import com.example.todo_list_v1.ui.navigation.NavigationDestination
import com.example.todo_list_v1.ui.theme.Todolistv1Theme

object CategoryManageDestination : NavigationDestination {
    override val route = "category_manage"
    override val titleRes = R.string.manage_categories
}

@Composable
fun CategoryManageScreen(
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            "Hello world"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryManageScreenPreview(
) {
    Todolistv1Theme {
        CategoryManageScreen()
    }
}