package com.example.todo_list_v1.ui.task.entry

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.todo_list_v1.data.category.Category
import com.example.todo_list_v1.ui.task.TaskDetails
import com.example.todo_list_v1.util.DateUtils.convertMillisToDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    modifier: Modifier = Modifier,
    taskDetails: TaskDetails,
    onValueChange: (TaskDetails) -> Unit = {},
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("No Date") }

    // Update selectedDate when taskDetails.dueDate changes
    LaunchedEffect(taskDetails.dueDate) {
        selectedDate = taskDetails.dueDate?.let { convertMillisToDate(it) } ?: "No Date"
    }

    SelectorItem(
        onClick = {
            showDatePicker = true
        },
        leadingIcon = LeadingIcon.VectorIcon(Icons.Default.DateRange),
        leadingText = "Date",
        trailingText = selectedDate,
        selector = {
            if (showDatePicker) {
                DatePickerModal(
                    onDateSelected = { date ->
                        selectedDate = date?.let { convertMillisToDate(it) } ?: "No date selected"
                        onValueChange(taskDetails.copy(dueDate = date))
                    },
                    onDismiss = { showDatePicker = false }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                // Debugging logs
                val selectedMillis = datePickerState.selectedDateMillis
                println("Selected millis (UTC): $selectedMillis")

                // Adjust the selected millis to local midnight
                val calendar = Calendar.getInstance().apply {
                    // Set to UTC time first, using the millis from DatePicker
                    timeZone = TimeZone.getTimeZone("UTC")
                    timeInMillis = selectedMillis ?: 0

                    // Adjust to the user's local timezone
                    timeZone = TimeZone.getDefault()

                    // Reset to local midnight if you want the date to start at 00:00 in local time
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                // Log the date as local time
                val formatter = SimpleDateFormat("EEE, MMM dd", Locale.getDefault())
                formatter.timeZone = TimeZone.getDefault()
                println("Formatted date (Local Time): ${formatter.format(calendar.time)}")
                println("Selected millis (Local time): ${calendar.timeInMillis}")
                // Send back local midnight millis
                onDateSelected(calendar.timeInMillis)
                onDismiss()
            }) {
                Text("DONE")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}