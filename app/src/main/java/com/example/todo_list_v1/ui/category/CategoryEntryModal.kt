package com.example.todo_list_v1.ui.category

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.todo_list_v1.R
import com.example.todo_list_v1.ui.task.TaskDetails
import com.example.todo_list_v1.ui.theme.Todolistv1Theme

@Composable
fun CategoryEntryModal(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    categoryUiState: CategoryUiState,
    onCategoryValueChange: (CategoryDetails) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "New Category") },
        text = {
            Column {
                TextField(
                    value = categoryUiState.categoryDetails.name,
                    placeholder = { Text(stringResource(R.string.category_entry_placeholder)) },
                    onValueChange = { }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm() },
                enabled = categoryUiState.isEntryValid
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() },
            ) {
                Text("Cancel")
            }
        }
    )
}



@Preview(showBackground = true)
@Composable
fun CategoryEntryModalPreview() {
    Todolistv1Theme{
        CategoryEntryModal(
            onDismiss = { /*TODO*/ },
            onConfirm = { /*TODO*/ },
            categoryUiState = CategoryUiState(),
            onCategoryValueChange = { },
            modifier = Modifier)
    }
}
