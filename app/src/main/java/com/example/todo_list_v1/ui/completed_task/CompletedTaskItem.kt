package com.example.todo_list_v1.ui.completed_task

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo_list_v1.R
import com.example.todo_list_v1.data.completed_task.CompletedTask
import com.example.todo_list_v1.ui.task.entry.convertMillisToDate
import com.example.todo_list_v1.ui.theme.Todolistv1Theme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CompletedTaskItem(completedTask: CompletedTask) {
    // Format the completion time
    val dateFormat = remember { SimpleDateFormat("h:mm a", Locale.getDefault()) }
    val formattedCompletionTime = dateFormat.format(Date(completedTask.completedAt))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_small)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Display the formatted completion time
        Text(
            text = formattedCompletionTime,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(end = dimensionResource(id = R.dimen.padding_small))
        )
        // Card containing task details
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.weight(1f) // Allow card to take remaining space
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_small)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = true,
                    onCheckedChange = {},
                    enabled = false
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_small)))
                Column {
                    Text(
                        text = completedTask.taskName,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.widthIn(max = 150.dp)
                    )
                    if (completedTask.taskDueDate != null) {
                        Text(
                            text = convertMillisToDate(completedTask.taskDueDate),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
                        )
                    }
                }
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More Options",
                    modifier = Modifier
                        .clickable {  }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompletedTaskItemPreview() {
    Todolistv1Theme {
        CompletedTaskItem(
            completedTask = CompletedTask(
                taskName = "11",
                completedAt = 1726691218190,
                taskDueDate = 1726691218190
            )
        )
    }
}
