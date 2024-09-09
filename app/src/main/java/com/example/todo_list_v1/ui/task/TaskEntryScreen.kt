package com.example.todo_list_v1.ui.task

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_list_v1.R
import com.example.todo_list_v1.TodoTopAppBar
import com.example.todo_list_v1.data.category.Category
import com.example.todo_list_v1.ui.AppViewModelProvider
import com.example.todo_list_v1.ui.navigation.NavigationDestination
import com.example.todo_list_v1.ui.theme.Todolistv1Theme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object TaskEntryDestination : NavigationDestination {
    override val route = "task_entry"
    override val titleRes = R.string.add_task
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: TaskEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val categories by viewModel.categories.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TodoTopAppBar(
                title = stringResource(R.string.task_entry_title),  // Update with the appropriate title resource
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        TaskEntryBody(
            taskUiState = viewModel.taskUiState,
            categories = categories,
            onTaskValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveTask()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun CategoryDropdown(
    showCategoryMenu: Boolean,
    selectedCategory: Category?,
    categories: List<Category>,
    onCategorySelected: (Category?) -> Unit,
    onDismissRequest: () -> Unit
) {
    DropdownMenu(
        expanded = showCategoryMenu,
        onDismissRequest = onDismissRequest,
        modifier = Modifier.fillMaxWidth()
    ) {
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
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEntryBody(
    taskUiState: TaskUiState,
    categories: List<Category>,
    onTaskValueChange: (TaskDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showCategoryMenu by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var selectedDate by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    // Handle date change when the picker is confirmed
    LaunchedEffect(datePickerState.selectedDateMillis) {
        selectedDate = datePickerState.selectedDateMillis?.let {
            convertMillisToDate(it)
        } ?: ""
    }

    Column(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_extra_large))
    ) {
        TaskInputForm(
            taskDetails = taskUiState.taskDetails,
            onValueChange = onTaskValueChange,
            modifier = Modifier
                .fillMaxWidth()
        )

        Column {
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clickable { showCategoryMenu = true }
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = "Select category",
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text(
                        text = "Category",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp),
                            )
                    ) {
                        Text(
                            text = selectedCategory?.name ?: "No Category",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(
                                    horizontal = dimensionResource(id = R.dimen.padding_small),
                                    vertical = dimensionResource(id = R.dimen.padding_tiny))
                        )
                    }
                }
            }
            // Use the CategoryDropdown composable
            CategoryDropdown(
                showCategoryMenu = showCategoryMenu,
                selectedCategory = selectedCategory,
                categories = categories,
                onCategorySelected = { category ->
                    selectedCategory = category
                    onTaskValueChange(taskUiState.taskDetails.copy(categoryId = category?.id))
                },
                onDismissRequest = { showCategoryMenu = false }
            )
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clickable {
                        showDatePicker = true
                    }
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date",
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text(
                        text = "Date",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = selectedDate.ifEmpty { "No Date" },
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp),
                            )
                            .padding(
                                horizontal = dimensionResource(id = R.dimen.padding_small),
                                vertical = dimensionResource(id = R.dimen.padding_tiny)
                            )
                    )

                }
            }
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clickable {

                    }
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.clock),
                        contentDescription = "Select date",
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text(
                        text = "Set Time",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "7:30 AM - 9:00 AM",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp),
                            )
                            .padding(
                                horizontal = dimensionResource(id = R.dimen.padding_small),
                                vertical = dimensionResource(id = R.dimen.padding_tiny)
                            )
                    )
                }
            }
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clickable {

                    }
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.repeat),
                        contentDescription = "Select date",
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text(
                        text = "Repeat Task",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Custom",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp),
                            )
                            .padding(
                                horizontal = dimensionResource(id = R.dimen.padding_small),
                                vertical = dimensionResource(id = R.dimen.padding_tiny)
                            )
                    )
                }
            }

            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clickable {

                    }
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Select date",
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text(
                        text = "Alarm",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "5 minutes before",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp),
                            )
                            .padding(
                                horizontal = dimensionResource(id = R.dimen.padding_small),
                                vertical = dimensionResource(id = R.dimen.padding_tiny)
                            )
                    )
                }
            }
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            OutlinedTextField(
                value = "Add Note",
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(196.dp)
                    .padding(top = dimensionResource(id = R.dimen.padding_medium))
            )
        }

        if (showDatePicker) {
            DatePickerModal(
                onDateSelected = { date ->
                    selectedDate = date?.let { convertMillisToDate(it) } ?: "No date selected"
                },
                onDismiss = { showDatePicker = false }
            )
        }

        Button(
            onClick = onSaveClick,
            enabled = taskUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}


fun convertMillisToDate(millis: Long?): String {
    return if (millis != null) {
        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        formatter.format(Date(millis))
    } else {
        "Invalid date"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                println("test")
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("DONE")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun TaskInputForm(
    taskDetails: TaskDetails,
    modifier: Modifier = Modifier,
    onValueChange: (TaskDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        OutlinedTextField(
            value = taskDetails.name,
            onValueChange = { onValueChange(taskDetails.copy(name = it)) },
            placeholder = {Text(stringResource(R.string.task_name_placeholder))},
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskEntryScreenPreview() {
    Todolistv1Theme {
        val categories = listOf<Category>()
        TaskEntryBody(
            taskUiState = TaskUiState(
                TaskDetails(
                    name = "Task name", description = "Task description"
                )
            ),
            onTaskValueChange = {},
            categories = categories,
            onSaveClick = {}
        )
    }
}