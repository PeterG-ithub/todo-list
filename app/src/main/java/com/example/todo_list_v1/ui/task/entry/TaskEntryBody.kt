package com.example.todo_list_v1.ui.task.entry

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
) {
    var ifDateSelected by remember { mutableStateOf(false) }
    var ifTimeSelected by remember { mutableStateOf(false) }

    LaunchedEffect(taskUiState.taskDetails.dueDate, taskUiState.taskDetails.expectedStartTime) {
        ifDateSelected = taskUiState.taskDetails.dueDate != null
        ifTimeSelected = taskUiState.taskDetails.expectedStartTime != null
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
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onBackground)
            CategorySelector(
                taskDetails = taskUiState.taskDetails,
                onValueChange = onTaskValueChange,
                modifier = Modifier,
                categories = categories
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onBackground)
            DateSelector(
                taskDetails = taskUiState.taskDetails,
                onValueChange = onTaskValueChange,
                modifier = Modifier,
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onBackground )
            TimeSelector(
                taskDetails = taskUiState.taskDetails,
                onValueChange = onTaskValueChange,
                modifier = Modifier,
                enable = ifDateSelected
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = if (ifDateSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f))

            SelectorItem(
                onClick = {
                    // Handle click event if needed
                },
                leadingIcon = LeadingIcon.PainterIcon(painterResource(id = R.drawable.repeat)),
                leadingText = "Repeat Task",
                trailingText = "No",
                selector = { },
                enabled = ifDateSelected
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = if (ifDateSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
            )
            SelectorItem(
                onClick = { /*TODO*/ },
                leadingIcon = LeadingIcon.VectorIcon(Icons.Default.Notifications),
                leadingText = "Alarm",
                trailingText = "Off",
                selector = { },
                enabled = ifDateSelected
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = if (ifDateSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
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
        modifier = Modifier
    )
}