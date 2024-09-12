package com.example.todo_list_v1.ui.task.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import com.example.todo_list_v1.R
import com.example.todo_list_v1.ui.task.TaskDetails
import com.example.todo_list_v1.ui.theme.Todolistv1Theme

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

@Composable
fun RepeatSelectionModal(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
) {
    val texts = listOf("Daily", "Weekly", "Monthly", "Yearly", "Custom")
    val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    var isSwitchChecked by remember { mutableStateOf(true) }
    var selectedRepeatOption by remember { mutableStateOf("Daily") }
    var showRepeatEveryDropdown by remember { mutableStateOf(false) }

    var selectedRepeatEveryOption = when (selectedRepeatOption) {
        "Daily" -> "1 day"
        "Weekly" -> "1 week"
        "Monthly" -> "1 month"
        "Yearly" -> "1 year"
        else -> ""
    }

    val repeatUnit = when (selectedRepeatOption) {
        "Daily" -> "day"
        "Weekly" -> "week"
        "Monthly" -> "month"
        "Yearly" -> "year"
        else -> ""
    }

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
                                text = "Never",
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
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0f),
                                        shape = CircleShape
                                    )
                                    .then(
                                        if (isSwitchChecked && selectedRepeatOption == "Weekly") {
                                            Modifier.clickable { }
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
                    TextButton(onClick = { /* Handle dismiss */ }) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { /* Handle confirm */ }) {
                        Text("OK")
                    }
                }
            }
        }
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



@Preview(showBackground = true)
@Composable
fun RepeatSelectionModalPreview() {
    Todolistv1Theme {
        Box(modifier = Modifier.fillMaxSize())
        RepeatSelectionModal()
    }
}