package com.example.todo_list_v1.ui.task.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.example.todo_list_v1.R
import com.example.todo_list_v1.data.task.Task
import com.example.todo_list_v1.ui.task.entry.convertMillisToDate
import com.example.todo_list_v1.ui.task.entry.convertMillisToDates
import com.example.todo_list_v1.ui.theme.Todolistv1Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TaskItemCard(
    task: Task,
    onTaskCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var isChecked by remember { mutableStateOf(task.isCompleted) }
    val scope = rememberCoroutineScope()

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_small)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { checked ->
                    // Update the checkbox UI first
                    isChecked = checked
                    // Delay the task deletion or update action
                    scope.launch {
                        delay(300L)

                        if (task.nextOccurrence != null) {
                            delay(100L) // Optional delay before unchecking for recurring task
                            isChecked = false // Uncheck after updating recurring task
                        }
                        onTaskCheckedChange(checked)
                    }

                }
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_small)))
            Column {
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.widthIn(max = 225.dp)
                )
                if (task.dueDate != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = convertMillisToDate(task.dueDate),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_tiny)))

                        if (task.repeatFrequency != null) {
                            Icon(
                                painter = painterResource(
                                    id = R.drawable.repeat),
                                contentDescription = "Repeat Icon",
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TaskItemCardPreview() {
    Todolistv1Theme {
        TaskItem(task = Task(name = "Preview Preview Preview Preview Preview Preview ", nextOccurrence = 1726467600000), onTaskCheckedChange = { true })
    }
}