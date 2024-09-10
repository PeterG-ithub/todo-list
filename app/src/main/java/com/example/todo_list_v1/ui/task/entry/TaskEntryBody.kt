package com.example.todo_list_v1.ui.task.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo_list_v1.R
import com.example.todo_list_v1.data.category.Category
import com.example.todo_list_v1.ui.task.TaskDetails
import com.example.todo_list_v1.ui.task.TaskUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEntryBody(
    taskUiState: TaskUiState,
    categories: List<Category>,
    onTaskValueChange: (TaskDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    onAddCategoryClick: () -> Unit = { }
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
        TaskEntryForm(
            taskDetails = taskUiState.taskDetails,
            onValueChange = onTaskValueChange,
            modifier = Modifier
                .fillMaxWidth()
        )

        Column {
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            CategorySelector(
                onClick = { showCategoryMenu = true},
                showCategoryMenu = showCategoryMenu,
                selectedCategory = selectedCategory,
                categories = categories,
                onCategorySelected = { category ->
                    selectedCategory = category
                    onTaskValueChange(taskUiState.taskDetails.copy(categoryId = category?.id))
                },
                onDismissRequest = { showCategoryMenu = false },
                onAddCategoryClick = {
                    onAddCategoryClick()
                }
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
            HorizontalDivider(
                thickness = 1.dp,
                color = if (selectedDate.isNotEmpty()) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clickable(enabled = selectedDate.isNotEmpty()) {
                        // Handle click
                    }
                    .background(
                        color = MaterialTheme.colorScheme.background
                    )
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.clock),
                        contentDescription = "Select date",
                        modifier = Modifier.padding(end = 16.dp),
                        tint = if (selectedDate.isNotEmpty()) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                    )
                    Text(
                        text = "Set Time",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (selectedDate.isNotEmpty()) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "No",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .background(
                                color = if (selectedDate.isNotEmpty()) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primaryContainer.copy(
                                    alpha = 0.6f
                                ),
                                shape = RoundedCornerShape(8.dp),
                            )
                            .padding(
                                horizontal = dimensionResource(id = R.dimen.padding_small),
                                vertical = dimensionResource(id = R.dimen.padding_tiny)
                            )
                    )
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = if (selectedDate.isNotEmpty()) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clickable(enabled = selectedDate.isNotEmpty()) {
                        // Handle click
                    }
                    .background(
                        color = if (selectedDate.isNotEmpty()) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.2f
                        )
                    )
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.repeat),
                        contentDescription = "Select date",
                        modifier = Modifier.padding(end = 16.dp),
                        tint = if (selectedDate.isNotEmpty()) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                    )
                    Text(
                        text = "Repeat Task",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (selectedDate.isNotEmpty()) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "No",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .background(
                                color = if (selectedDate.isNotEmpty()) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primaryContainer.copy(
                                    alpha = 0.6f
                                ),
                                shape = RoundedCornerShape(8.dp),
                            )
                            .padding(
                                horizontal = dimensionResource(id = R.dimen.padding_small),
                                vertical = dimensionResource(id = R.dimen.padding_tiny)
                            )
                    )
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = if (selectedDate.isNotEmpty()) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clickable(enabled = selectedDate.isNotEmpty()) {
                        // Handle click
                    }
                    .background(
                        color = if (selectedDate.isNotEmpty()) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.2f
                        )
                    )
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Select date",
                        modifier = Modifier.padding(end = 16.dp),
                        tint = if (selectedDate.isNotEmpty()) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                    )
                    Text(
                        text = "Alarm",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (selectedDate.isNotEmpty()) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "5 minutes before",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .background(
                                color = if (selectedDate.isNotEmpty()) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primaryContainer.copy(
                                    alpha = 0.6f
                                ),
                                shape = RoundedCornerShape(8.dp),
                            )
                            .padding(
                                horizontal = dimensionResource(id = R.dimen.padding_small),
                                vertical = dimensionResource(id = R.dimen.padding_tiny)
                            )
                    )
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = if (selectedDate.isNotEmpty()) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
            )
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

@Composable
fun CategorySelector(
    onClick: () -> Unit,
    selectedCategory: Category?,
    showCategoryMenu: Boolean,
    categories: List<Category>,
    onCategorySelected: (Category?) -> Unit,
    onDismissRequest: () -> Unit,
    onAddCategoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SelectorItem(
        onClick = { onClick() },
        leadingIcon = LeadingIcon.VectorIcon(Icons.Default.List),
        leadingText = "Category",
        trailingText = selectedCategory?.name ?: "No Category",
        selector = {
            CategoryDropdown(
                showCategoryMenu = showCategoryMenu,
                selectedCategory = selectedCategory,
                categories = categories,
                onCategorySelected = onCategorySelected,
                onDismissRequest = onDismissRequest,
                onAddCategoryClick = onAddCategoryClick
            )
        },
        enabled = true,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun TaskEntryBodyPreview() {
    val sampleCategories = listOf(
        Category(id = 1, name = "Work"),
        Category(id = 2, name = "Personal"),
        Category(id = 3, name = "Hobby")
    )

    val sampleTaskUiState = TaskUiState(
        taskDetails = TaskDetails(
            name = "Sample Task",
            description = "This is a sample task description.",
            categoryId = null
        ),
        isEntryValid = true
    )

    TaskEntryBody(
        taskUiState = sampleTaskUiState,
        categories = sampleCategories,
        onTaskValueChange = { /* Handle task value change */ },
        onSaveClick = { /* Handle save click */ },
        modifier = Modifier.fillMaxSize(),
        onAddCategoryClick = { /* Handle add category click */ }
    )
}