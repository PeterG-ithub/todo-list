package com.example.todo_list_v1.ui.task.entry

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.todo_list_v1.R
import com.example.todo_list_v1.ui.task.TaskDetails

@Composable
fun RepeatSelector(
    modifier: Modifier = Modifier,
    taskDetails: TaskDetails,
    onValueChange: (TaskDetails) -> Unit = {},
    enable: Boolean = true,
) {
    SelectorItem(
        onClick = {
            // Handle click event if needed
        },
        leadingIcon = LeadingIcon.PainterIcon(painterResource(id = R.drawable.repeat)),
        leadingText = "Repeat Task",
        trailingText = "No",
        selector = { },
        enabled = enable
    )
}