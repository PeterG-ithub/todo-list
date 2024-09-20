package com.example.todo_list_v1.ui.category.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.todo_list_v1.R
import com.example.todo_list_v1.data.category.Category
import com.example.todo_list_v1.ui.category.item.CategoryItem
import com.example.todo_list_v1.ui.theme.Todolistv1Theme

@Composable
fun CategoryList(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit,
    onDeleteClick: (Category) -> Unit,
    onVisibilityClick: (Category) -> Unit,
) {
    LazyColumn {
        items(categories) { category ->
            CategoryItem(
                category = category,
                visible = true,
                modifier = Modifier
                    .clickable { onCategoryClick(category) }
                    .padding(
                        vertical = dimensionResource(id = R.dimen.padding_tiny),
                        horizontal = dimensionResource(id = R.dimen.padding_small)
                    ),
                onDeleteClick = { onDeleteClick(category) },
                onVisibilityClick = { onVisibilityClick(category) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryListPreview() {
    Todolistv1Theme {
        CategoryList(
            categories = listOf(
                Category(name = "Category 1"),
                Category(name = "Category 2")
            ),
            onCategoryClick = { },
            onDeleteClick = { },
            onVisibilityClick = { }
        )
    }
}