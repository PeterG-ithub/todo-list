package com.example.todo_list_v1.ui.category

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_list_v1.R
import com.example.todo_list_v1.ui.AppViewModelProvider
import com.example.todo_list_v1.ui.theme.Todolistv1Theme
import kotlinx.coroutines.launch

@Composable
fun CategoryEntryModal(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CategoryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    // Fetch categories and reset the category details when the modal opens
    LaunchedEffect(Unit) {
        viewModel.fetchAllCategories()  // Ensure categories are fetched
        viewModel.updateUiState(CategoryDetails(name = ""))  // Clear the input
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "New Category") },
        text = {
            Column {
                TextField(
                    value = viewModel.categoryUiState.categoryDetails.name,
                    placeholder = { Text(stringResource(R.string.category_entry_placeholder)) },
                    onValueChange = { newName ->
                        val updatedDetails = viewModel.categoryUiState.categoryDetails.copy(name = newName)
                        viewModel.updateUiState(updatedDetails)
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveCategory()
                        onConfirm()
                    }
                },
                enabled = viewModel.categoryUiState.isEntryValid
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
        },
        modifier = modifier
    )
}



@Preview(showBackground = true)
@Composable
fun CategoryEntryModalPreview() {
    Todolistv1Theme{
        CategoryEntryModal(
            onDismiss = { /*TODO*/ },
            onConfirm = { /*TODO*/ },
            modifier = Modifier)
    }
}
