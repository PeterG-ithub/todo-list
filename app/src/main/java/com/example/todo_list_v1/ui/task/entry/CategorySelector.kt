package com.example.todo_list_v1.ui.task.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.todo_list_v1.R
import com.example.todo_list_v1.data.category.Category
import com.example.todo_list_v1.ui.category.CategoryEntryModal
import com.example.todo_list_v1.ui.task.TaskDetails
import com.example.todo_list_v1.ui.theme.Todolistv1Theme

@Composable
fun CategorySelector(
    taskDetails: TaskDetails,
    modifier: Modifier = Modifier,
    categories: List<Category>,
    onValueChange: (TaskDetails) -> Unit = {},
) {
    var showCategoryMenu by remember { mutableStateOf(false) }
    val showCategoryEntryModal = remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    fun getCategoryNameById(id: Int?): String {
        return categories.find { it.id == id }?.name ?: "No Category"
    }

    println(categories)
    SelectorItem(
        onClick = { showCategoryMenu = true },
        leadingIcon = LeadingIcon.VectorIcon(Icons.Default.List),
        leadingText = "Category",
        trailingText = getCategoryNameById(taskDetails.categoryId),
        selector = {
            CategoryDropdown(
                showCategoryMenu = showCategoryMenu,
                selectedCategory = selectedCategory,
                categories = categories,
                onCategorySelected = { category ->
                    selectedCategory = category
                    onValueChange(taskDetails.copy(categoryId = category?.id)) },
                onDismissRequest = { showCategoryMenu = false },
                onAddCategoryClick = { showCategoryEntryModal.value = true }
            )
        },
        enabled = true,
        modifier = modifier
    )
    if (showCategoryEntryModal.value) {
        CategoryEntryModal(
            onDismiss = { showCategoryEntryModal.value = false },
            onConfirm = { showCategoryEntryModal.value = false }
        )
    }
}

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

@Preview(showBackground = true)
@Composable
fun CategorySelectorPreview() {
    Todolistv1Theme {
        CategorySelector(taskDetails = TaskDetails(), categories = listOf(Category(name = "Test")))
    }
}