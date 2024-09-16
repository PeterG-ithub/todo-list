package com.example.todo_list_v1.ui.task.entry

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import com.example.todo_list_v1.R
import com.example.todo_list_v1.ui.task.TaskDetails
import com.example.todo_list_v1.ui.theme.Todolistv1Theme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RepeatSelector(
    modifier: Modifier = Modifier,
    taskDetails: TaskDetails,
    onValueChange: (TaskDetails) -> Unit = {},
    enable: Boolean = true,
) {
    var showRepeatModal by remember { mutableStateOf(false)}
    SelectorItem(
        onClick = {
            showRepeatModal = true
        },
        leadingIcon = LeadingIcon.PainterIcon(painterResource(id = R.drawable.repeat)),
        leadingText = "Repeat Task",
        trailingText = getRepeatText(taskDetails),
        selector = {
            if (showRepeatModal) {
                RepeatSelectionModal(
                    modifier = modifier,
                    taskDetails = taskDetails,
                    onValueChange = onValueChange,
                    onDismiss = {
                        showRepeatModal = false
                    }
                )
            }
        },
        enabled = enable
    )
}

fun getRepeatText(taskDetails: TaskDetails): String {
    return when (taskDetails.repeatFrequency) {
        "Daily" -> {
            if (taskDetails.repeatInterval == 1) "Every day"
            else "Every ${taskDetails.repeatInterval} days"
        }
        "Weekly" -> {
            taskDetails.repeatOnDays?.let { days ->
                val intervalText = if (taskDetails.repeatInterval == 1) "Every week"
                else "Every ${taskDetails.repeatInterval} weeks"

                if (days.isEmpty()) {
                    intervalText // Return interval text alone if no specific days are selected
                } else {
                    // Get day names from indices and join them
                    val dayNames = days.joinToString(", ") { getDayName(it) }
                    "$intervalText on $dayNames" // Combine interval and days text
                }
            } ?: "Every week"
        }
        "Monthly" -> {
            if (taskDetails.repeatInterval == 1) "Every month"
            else "Every ${taskDetails.repeatInterval} months"
        }
        "Yearly" -> {
            if (taskDetails.repeatInterval == 1) "Every year"
            else "Every ${taskDetails.repeatInterval} years"
        }
        else -> "No"
    }
}

fun getDayName(dayIndex: Int): String {
    return when (dayIndex) {
        0 -> "Sun"
        1 -> "Mon"
        2 -> "Tue"
        3 -> "Wed"
        4 -> "Thu"
        5 -> "Fri"
        6 -> "Sat"
        else -> ""
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RepeatSelectionModal(
    modifier: Modifier = Modifier,
    taskDetails: TaskDetails,
    onValueChange: (TaskDetails) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    val texts = listOf("Daily", "Weekly", "Monthly", "Yearly", "Custom")
    val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    var isSwitchChecked by remember { mutableStateOf(true) }
    var selectedRepeatOption by remember { mutableStateOf(taskDetails.repeatFrequency ?: "Daily") }
    var showRepeatEveryDropdown by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    var repeatEndsAtDate by remember { mutableStateOf(convertMillisToDates(taskDetails.repeatEndsAt)) }

    var selectedRepeatEveryOption = when (selectedRepeatOption) {
        "Daily" -> "${taskDetails.repeatInterval ?: 1} day${if (taskDetails.repeatInterval != 1) "s" else ""}"
        "Weekly" -> "${taskDetails.repeatInterval ?: 1} week${if (taskDetails.repeatInterval != 1) "s" else ""}"
        "Monthly" -> "${taskDetails.repeatInterval ?: 1} month${if (taskDetails.repeatInterval != 1) "s" else ""}"
        "Yearly" -> "${taskDetails.repeatInterval ?: 1} year${if (taskDetails.repeatInterval != 1) "s" else ""}"
        else -> ""
    }

    val repeatUnit = when (selectedRepeatOption) {
        "Daily" -> "day"
        "Weekly" -> "week"
        "Monthly" -> "month"
        "Yearly" -> "year"
        else -> ""
    }
    val repeatOnDays = taskDetails.repeatOnDays ?: emptyList()
    val toggleDayState = remember { mutableStateMapOf<String, Boolean>().apply {
        days.forEachIndexed { index, day ->
            this[day] = repeatOnDays.contains(index)
        }
    }}

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp) // Add your custom padding here
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "Repeat Task", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = isSwitchChecked,
                        onCheckedChange = { isChecked ->
                            isSwitchChecked = isChecked
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                            uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                            uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                        )
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
                ) {
                    Spacer(modifier = Modifier)
                    texts.forEach { text ->
                        Box(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(size = 4.dp)) // Clip the whole Box to the shape
                                .background(
                                    color = if (selectedRepeatOption == text) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(size = 4.dp) // Background color with shape
                                )
                                .clickable {
                                    selectedRepeatOption = text
                                }
                                .padding(
                                    vertical = dimensionResource(id = R.dimen.padding_small),
                                    horizontal = 10.dp // Apply padding inside the Box
                                )
                        ) {
                            Text(
                                text = text,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (selectedRepeatOption == text) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    Spacer(modifier = Modifier)
                }
                //Repeat every box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (isSwitchChecked) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground.copy(
                                alpha = 0.1f
                            )
                        )
                        .then(
                            if (isSwitchChecked) {
                                Modifier.clickable {
                                    showRepeatEveryDropdown = true
                                }
                            } else {
                                Modifier
                            }
                        )
                        .padding(
                            vertical = 10.dp,
                            horizontal = 14.dp
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Repeat every",
                            color = if (isSwitchChecked) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(8.dp),
                                )
                        ) {
                            Text(
                                text = selectedRepeatEveryOption,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isSwitchChecked) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                                modifier = Modifier
                                    .padding(
                                        horizontal = 10.dp,
                                        vertical = 6.dp
                                    )
                            )
                        }
                        RepeatEveryDropdown(
                            showMenu = showRepeatEveryDropdown,
                            onDismissRequest = { showRepeatEveryDropdown = false },
                            onItemSelected = { option ->
                                selectedRepeatEveryOption = option
                            },
                            selectedOption = repeatUnit
                        )
                    }
                }
                // Repeat ends at Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (isSwitchChecked) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground.copy(
                                alpha = 0.1f
                            )
                        )
                        .then(
                            if (isSwitchChecked) {
                                Modifier.clickable {
                                    showDatePicker = true
                                }
                            } else {
                                Modifier
                            }
                        )
                        .padding(
                            vertical = 10.dp,
                            horizontal = 14.dp
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Repeat ends at",
                            color = if (isSwitchChecked) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(8.dp),
                                )
                        ) {
                            Text(
                                text = repeatEndsAtDate,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isSwitchChecked) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                                modifier = Modifier
                                    .padding(
                                        horizontal = 10.dp,
                                        vertical = 6.dp
                                    )
                            )
                        }
                    }
                }
                // Repeat On Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (isSwitchChecked && selectedRepeatOption == "Weekly") MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground.copy(
                                alpha = 0.1f
                            )
                        )
                        .padding(
                            vertical = 10.dp,
                            horizontal = 14.dp
                        )
                ) {
                    Row {
                        Text(
                            text = "Repeat on",
                            color = if (isSwitchChecked && selectedRepeatOption == "Weekly") MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (isSwitchChecked && selectedRepeatOption == "Weekly") MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground.copy(
                                alpha = 0.1f
                            )
                        )
                        .padding(
                            top = 4.dp,
                            bottom = 16.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(8.dp), // Space between each Box
                        verticalAlignment = Alignment.CenterVertically // Center vertically if needed
                    ) {
                        days.forEach { day ->
                            val isDaySelected = toggleDayState[day] ?: false
                            Box(
                                modifier = Modifier
                                    .size(32.dp) // Fixed size for the Box
                                    .clip(CircleShape) // Clip to CircleShape for clickable area
                                    .border(
                                        width = 1.dp, // Border width
                                        color = if (isSwitchChecked && selectedRepeatOption == "Weekly") MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(
                                            alpha = 0.3f
                                        ),
                                        shape = CircleShape // Border shape
                                    )
                                    .background(
                                        color = if (isDaySelected) MaterialTheme.colorScheme.primaryContainer.copy(
                                            alpha = 1f
                                        ) else MaterialTheme.colorScheme.onBackground.copy(alpha = 0f),
                                        shape = CircleShape
                                    )
                                    .then(
                                        if (isSwitchChecked && selectedRepeatOption == "Weekly") {
                                            Modifier.clickable {
                                                toggleDayState[day] = !isDaySelected
                                            }
                                        } else {
                                            Modifier
                                        }
                                    ),
                                contentAlignment = Alignment.Center // Center content in the Box
                            ) {
                                Text(
                                    text = day,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isSwitchChecked && selectedRepeatOption == "Weekly") MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 4.dp
                        ),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = {
                        if (isSwitchChecked) {
                            val repeatInterval = selectedRepeatEveryOption.split(" ")[0].toIntOrNull()
                            val selectedRepeatOnDays = if (selectedRepeatOption == "Weekly") {
                                toggleDayState.filter { it.value }
                                    .keys
                                    .map { day -> days.indexOf(day) }
                                    .sorted() // Sort indices in ascending order
                            } else null

                            val repeatEndsAt = convertDateToMillis(repeatEndsAtDate)
                            onValueChange(
                                taskDetails.copy(
                                    repeatFrequency = selectedRepeatOption, // This should now have the correct value
                                    repeatInterval = repeatInterval,
                                    repeatEndsAt = repeatEndsAt,
                                    repeatOnDays = selectedRepeatOnDays,
                                    nextOccurrence = calculateNextOccurrence(selectedRepeatOption, repeatInterval, repeatEndsAt, repeatOnDays)
                                )
                            )
                        } else {
                            onValueChange(
                                taskDetails.copy(
                                    repeatFrequency = null,
                                    repeatInterval = null,
                                    repeatEndsAt = null,
                                    repeatOnDays = null,
                                    nextOccurrence = null
                                )
                            )
                        }
                        onDismiss()
                    }) {
                        Text("OK")
                    }
                }
            }
        }
    }
    if (showDatePicker) {
        RepeatEndsAtModal(
            onDismissRequest = { showDatePicker = false },
            onDateSelected = { date ->
                repeatEndsAtDate = date
            }
        )
    }
}

@Composable
fun RepeatEveryDropdown(
    modifier: Modifier = Modifier,
    showMenu: Boolean,
    onDismissRequest: () -> Unit,
    onItemSelected: (String) -> Unit,
    selectedOption: String,
) {
    val repeatOptions = listOf("1 $selectedOption") + (2..365).map { "$it ${selectedOption}s" }

    if (showMenu) {
        Popup(
            alignment = Alignment.TopEnd,
            offset = IntOffset(x = 0, y = 90),
            onDismissRequest = { onDismissRequest() }
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .border(1.dp, Color.Gray)
                    .padding(vertical = 8.dp)
                    .height(200.dp)
                    .widthIn(max = 105.dp)
            ) {
                LazyColumn {
                    items(repeatOptions) { option ->
                        // Custom item layout
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onItemSelected(option)
                                    onDismissRequest()
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp) // Padding inside the item
                        ) {
                            Text(
                                text = option,
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepeatEndsAtModal(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    // Create a DatePickerState
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {

                onDateSelected(convertMillisToDates(datePickerState.selectedDateMillis))
                onDismissRequest()
            }) {
                Text("DONE")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDateSelected("Never")
                onDismissRequest()
            }) {
                Box(
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp)
                ) {
                    Text(
                        text = "NEVER",
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                }

            }
        },
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    text ="Repeat End by",
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                )
            },
        )
    }
}


fun convertMillisToDates(millis: Long?): String {
    return if (millis != null) {
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return formatter.format(Date(millis))
    } else {
        "Never"
    }
}
fun convertDateToMillis(date: String): Long? {
    if (date == "Never") {
        return null
    }
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.parse(date)?.time ?: System.currentTimeMillis() // Return current time if parsing fails
}

fun calculateNextOccurrence(
    selectedRepeatOption: String?,
    repeatInterval: Int?,
    repeatEndsAt: Long?,
    repeatOnDays: List<Int>?
): Long? {
    val calendar = Calendar.getInstance()

    // Handle different repeat frequencies
    val nextOccurrence = when (selectedRepeatOption?.lowercase()) {
        "daily" -> calculateNextDailyOccurrence(calendar, repeatInterval ?: 1)
        "weekly" -> calculateNextWeeklyOccurrence(calendar, repeatInterval ?: 1, repeatOnDays)
        "monthly" -> calculateNextMonthlyOccurrence(calendar, repeatInterval ?: 1)
        "yearly" -> calculateNextYearlyOccurrence(calendar, repeatInterval ?: 1)
        else -> null
    }

    // If the next occurrence exceeds repeatEndsAt, return null
    if (repeatEndsAt != null && nextOccurrence != null && nextOccurrence > repeatEndsAt) {
        return null
    }

    return nextOccurrence
}

fun calculateNextDailyOccurrence(calendar: Calendar, repeatInterval: Int): Long {
    // Move the calendar forward by the interval in days
    calendar.add(Calendar.DAY_OF_YEAR, repeatInterval)
    return calendar.timeInMillis
}

fun calculateNextWeeklyOccurrence(calendar: Calendar, repeatInterval: Int, repeatOnDays: List<Int>?): Long {
    if (repeatOnDays.isNullOrEmpty()) {
        // If no specific days are selected, default to moving forward by weeks
        calendar.add(Calendar.WEEK_OF_YEAR, repeatInterval)
    } else {
        // Move forward to the next valid day in repeatOnDays
        val currentDay = calendar.get(Calendar.DAY_OF_WEEK) - 1 // Calendar.DAY_OF_WEEK starts from 1 (Sunday)
        val sortedDays = repeatOnDays.sorted()

        // Find the next day in the list after the current day
        val nextDay = sortedDays.firstOrNull { it > currentDay } ?: sortedDays.first()

        // Calculate days until the next occurrence
        val daysToAdd = if (nextDay > currentDay) {
            nextDay - currentDay
        } else {
            (7 - currentDay) + nextDay
        }

        calendar.add(Calendar.DAY_OF_YEAR, daysToAdd)
    }
    return calendar.timeInMillis
}

fun calculateNextMonthlyOccurrence(calendar: Calendar, repeatInterval: Int): Long {
    // Move the calendar forward by the interval in months
    calendar.add(Calendar.MONTH, repeatInterval)
    return calendar.timeInMillis
}

fun calculateNextYearlyOccurrence(calendar: Calendar, repeatInterval: Int): Long {
    // Move the calendar forward by the interval in years
    calendar.add(Calendar.YEAR, repeatInterval)
    return calendar.timeInMillis
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun RepeatSelectionModalPreview() {
    Todolistv1Theme {
        Box(modifier = Modifier.fillMaxSize())
        RepeatSelectionModal(
            taskDetails = TaskDetails()
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun RepeatEndsAtModalPreview() {
    Todolistv1Theme {
        Box(modifier = Modifier.fillMaxSize())
        RepeatEndsAtModal(onDismissRequest = { /*TODO*/ }) {

        }
    }
}