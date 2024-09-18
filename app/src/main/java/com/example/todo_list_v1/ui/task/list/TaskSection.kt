package com.example.todo_list_v1.ui.task.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo_list_v1.R
import com.example.todo_list_v1.data.task.Task
import com.example.todo_list_v1.ui.theme.Todolistv1Theme

@Composable
fun TaskSection(
    title: String,
    taskList: List<Task>,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onTaskClick: (Int) -> Unit,
    onTaskCheckedChange: (Task, Boolean) -> Unit,
    onTaskDeleteClick: (Task) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    if (taskList.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggleExpand() }
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "$title (${taskList.size})",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
        }
        if (isExpanded) {
            TaskList(
                taskList = taskList,
                onTaskClick = { onTaskClick(it.id) },
                onTaskDeleteClick = onTaskDeleteClick,
                onTaskCheckedChange = onTaskCheckedChange,
                contentPadding = contentPadding,
                modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskSectionPreview() {
    val sampleTasks = listOf(
        Task(id = 1, name = "Buy groceries", isCompleted = false, dueDate = null),
        Task(id = 2, name = "Call plumber", isCompleted = true, dueDate = null),
        Task(id = 3, name = "Finish project", isCompleted = false, dueDate = null)
    )

    Todolistv1Theme {
        TaskSection(
            title = "Tasks",
            taskList = sampleTasks,
            isExpanded = true, // Show tasks in preview
            onToggleExpand = { /* Toggle logic */ },
            onTaskClick = { /* Handle task click */ },
            onTaskCheckedChange = { task, isChecked -> /* Handle task checked change */ },
            onTaskDeleteClick = { /* Handle task delete */ },
            modifier = Modifier.padding(top = 36.dp) // Ensure consistent padding
        )
    }
}
