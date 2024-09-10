package com.example.todo_list_v1.ui.task.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.todo_list_v1.R
import com.example.todo_list_v1.data.category.Category

@Composable
fun CategoryDropdown(
    showCategoryMenu: Boolean,
    selectedCategory: Category?,
    categories: List<Category>,
    onCategorySelected: (Category?) -> Unit,
    onDismissRequest: () -> Unit,
    onAddCategoryClick: () -> Unit,
) {
    DropdownMenu(
        expanded = showCategoryMenu,
        onDismissRequest = onDismissRequest,
        offset = DpOffset(x = 0.dp, y = dimensionResource(id = R.dimen.padding_tiny)),
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(
                horizontal = dimensionResource(id = R.dimen.padding_tiny)
            )
    ) {
        DropdownMenuItem(
            text = {
                Text(text = "No Category")
            },
            onClick = {
                onCategorySelected(null)
                onDismissRequest()
            },
            modifier = Modifier
        )
        categories.forEach { category ->
            DropdownMenuItem(
                text = {
                    Text(text = category.name)
                },
                onClick = {
                    onCategorySelected(category)
                    onDismissRequest()
                }
            )
        }
        DropdownMenuItem(
            text = {
                Text(
                    text = "Add Category",
                    color = MaterialTheme.colorScheme.primary,
                )
            },
            trailingIcon = {
                Icon(
                    imageVector =  Icons.Default.Add,
                    contentDescription = null
                )
            },
            onClick = {
                onAddCategoryClick()
            }
        )
    }
}
