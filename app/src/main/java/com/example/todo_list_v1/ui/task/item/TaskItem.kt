package com.example.todo_list_v1.ui.task.item

import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.todo_list_v1.R
import com.example.todo_list_v1.data.task.Task
import com.example.todo_list_v1.ui.theme.Todolistv1Theme
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskItem(
    task: Task,
    onTaskCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = { },
) {
    val density = LocalDensity.current

    val defaultActionSize = 80.dp
    val endActionSizePx = with(density) { (defaultActionSize).toPx() }

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Center,
            anchors = DraggableAnchors {
                DragAnchors.Center at 0f
                DragAnchors.End at -endActionSizePx
            },
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            snapAnimationSpec = tween(),
            decayAnimationSpec = exponentialDecay()
        )
    }

    DraggableItem(state = state,
        endAction = {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(dimensionResource(id = R.dimen.padding_tiny))
                    .clip(
                        RoundedCornerShape(
                            topEnd = 16.dp,
                            bottomEnd = 16.dp
                        )
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DeleteAction(
                    onDeleteClicked = { onDeleteClick() },
                    modifier = Modifier
                        .width(defaultActionSize + 8.dp)
                )
            }
        }, content = {
            TaskItemCard(
                task = task,
                onTaskCheckedChange,
                modifier
            )
        }
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
