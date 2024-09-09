package com.example.todo_list_v1.ui.task.item

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo_list_v1.R
import com.example.todo_list_v1.data.task.Task
import com.example.todo_list_v1.ui.theme.Todolistv1Theme

@Composable
fun TaskItem(
    task: Task,
    onTaskCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    TaskItemCard(
        task = task,
        onTaskCheckedChange,
        modifier
    )
}


@Preview(showBackground = true)
@Composable
fun TaskItemPreview() {
    Todolistv1Theme {
        TaskItem(
            task = Task(
                id = 1,
                name = "Sample Task",
                description = "This is a sample task description.",
                isCompleted = false // Add this property to match your Task data class
            ),
            onTaskCheckedChange = { /* no-op */ } // Stub for the preview
        )
    }
}
