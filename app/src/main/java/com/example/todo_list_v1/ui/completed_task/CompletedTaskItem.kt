package com.example.todo_list_v1.ui.completed_task

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.todo_list_v1.R
import com.example.todo_list_v1.data.completed_task.CompletedTask
import com.example.todo_list_v1.ui.theme.Todolistv1Theme
import com.example.todo_list_v1.util.DateUtils.convertMillisToDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CompletedTaskItem(
    completedTask: CompletedTask,
    onUndo: (CompletedTask) -> Unit = { },
    onDelete: (CompletedTask) -> Unit = { }
) {
    val dateFormat = remember { SimpleDateFormat("h:mm a", Locale.getDefault()) }
    val formattedCompletionTime = dateFormat.format(Date(completedTask.completedAt))

    var expanded by remember { mutableStateOf(false) }

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
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textDecoration = TextDecoration.LineThrough
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f),
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
                Box(
                    modifier = Modifier
                        .background(Color.Transparent, CircleShape)
                        .clip(CircleShape)
                        .clickable { expanded = !expanded }
                        .padding(dimensionResource(id = R.dimen.padding_tiny))
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options",
                        modifier = Modifier
                            .background(Color.Transparent, CircleShape)
                            .clip(CircleShape)
                            .clickable { expanded = !expanded }
                            .padding(dimensionResource(id = R.dimen.padding_small))
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Undo"
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Undo")
                                }
                            },
                            onClick = {
                                // Handle Undo action
                                onUndo(completedTask)
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Delete",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            onClick = {
                                // Handle Delete action
                                onDelete(completedTask)
                                expanded = false
                            }
                        )
                    }
                }
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
