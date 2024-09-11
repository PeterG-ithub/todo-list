package com.example.todo_list_v1.ui.task.entry

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.todo_list_v1.ui.task.TaskDetails

@Composable
fun AlarmSelector(
    modifier: Modifier = Modifier,
    taskDetails: TaskDetails,
    onValueChange: (TaskDetails) -> Unit = { },
    enable: Boolean = true,
) {
    SelectorItem(
        onClick = { /*TODO*/ },
        leadingIcon = LeadingIcon.VectorIcon(Icons.Default.Notifications),
        leadingText = "Alarm",
        trailingText = "Off",
        selector = { },
        enabled = enable
    )
}