package com.example.todo_list_v1.ui.task.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo_list_v1.R
import com.example.todo_list_v1.data.task.Task
import com.example.todo_list_v1.ui.task.item.TaskItem
import com.example.todo_list_v1.ui.theme.Todolistv1Theme

@Composable
fun TaskList(
    taskList: List<Task>,
    onTaskClick: (Task) -> Unit,
    onTaskCheckedChange: (Task, Boolean) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    onTaskDeleteClick: (Task) -> Unit = { }
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(items = taskList, key = { it.id }) { task ->
            TaskItem(
                task = task,
                onTaskCheckedChange = { isChecked ->
                    onTaskCheckedChange(task, isChecked)
                },
                onDeleteClick = { onTaskDeleteClick(task) },
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_tiny))
                    .clickable { onTaskClick(task) }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TaskListPreview() {
    Todolistv1Theme {
        TaskList(
            taskList = listOf(
                Task(id = 1, name = "Sample Task 1", description = "This is a sample task description.", isCompleted = false),
                Task(id = 2, name = "Sample Task 2", description = "This is another task description.", isCompleted = true)
            ),
            onTaskClick = { /* Handle task click */ },
            onTaskCheckedChange = { task, isChecked ->
                // Handle checkbox state change
            },
            contentPadding = PaddingValues(8.dp)
        )
    }
}
