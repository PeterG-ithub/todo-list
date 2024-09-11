package com.example.todo_list_v1.ui.task.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo_list_v1.R
import com.example.todo_list_v1.ui.theme.Todolistv1Theme

@Composable
fun SelectorItem(
    onClick: () -> Unit,
    leadingIcon: LeadingIcon,
    modifier: Modifier = Modifier,
    leadingText: String = "Placeholder",
    trailingText: String = "Placeholder",
    selector: @Composable () -> Unit = { },
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .then(
                if (enabled) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            )
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            when (leadingIcon) {
                is LeadingIcon.VectorIcon -> {
                    Icon(
                        imageVector = leadingIcon.imageVector,
                        contentDescription = "$leadingText Icon",
                        tint = if (enabled) MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
                is LeadingIcon.PainterIcon -> {
                    Icon(
                        painter = leadingIcon.painter,
                        contentDescription = "$leadingText Icon",
                        tint = if (enabled) MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }
            Text(
                text = leadingText,
                style = MaterialTheme.typography.bodyLarge,
                color = if (enabled) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
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
                    text = trailingText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (enabled) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.padding_small),
                            vertical = dimensionResource(id = R.dimen.padding_tiny)
                        )
                )
                selector()
            }
        }
    }
}

sealed class LeadingIcon {
    data class VectorIcon(val imageVector: ImageVector) : LeadingIcon()
    data class PainterIcon(val painter: Painter) : LeadingIcon()
}

@Preview(showBackground = true)
@Composable
fun SelectorItemPreview() {
    Todolistv1Theme {
        SelectorItem(
            onClick = { },
            leadingIcon = LeadingIcon.VectorIcon(Icons.Default.List),
            leadingText = "Category",
            trailingText = "No Category",
            enabled = false,
        )
    }
}