package com.example.todo_list_v1.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo_list_v1.R
import com.example.todo_list_v1.data.Task
import com.example.todo_list_v1.ui.AppViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_list_v1.data.TasksRepository
import com.example.todo_list_v1.ui.navigation.NavigationDestination
import com.example.todo_list_v1.ui.theme.Todolistv1Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf


object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToTaskEntry: () -> Unit,
    navigateToTaskUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(HomeDestination.titleRes)) },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = stringResource(R.string.more_options))
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToTaskEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .padding(
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(LocalLayoutDirection.current)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_task)
                )
            }
        }
    ) { innerPadding ->
        HomeBody(
            taskList = homeUiState.taskList,
            onTaskClick = navigateToTaskUpdate,
            onTaskCheckedChange = { task, isChecked ->
                // Add logic to handle checkbox state changes if needed
            },
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}

@Composable
private fun HomeBody(
    taskList: List<Task>,
    onTaskClick: (Int) -> Unit,
    onTaskCheckedChange: (Task, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (taskList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_tasks_here),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            TaskList(
                taskList = taskList,
                onTaskClick = { onTaskClick(it.id) },
                onTaskCheckedChange = onTaskCheckedChange,
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun TaskList(
    taskList: List<Task>,
    onTaskClick: (Task) -> Unit,
    onTaskCheckedChange: (Task, Boolean) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = taskList, key = { it.id }) { task ->
            TaskItem(
                task = task,
                onTaskCheckedChange = { isChecked ->
                    onTaskCheckedChange(task, isChecked)
                },
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onTaskClick(task) }
            )
        }
    }
}

@Composable
private fun TaskItem(
    task: Task,
    onTaskCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_small)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { isChecked ->
                    onTaskCheckedChange(isChecked)
                }
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_small)))
            Text(text = task.name, style = MaterialTheme.typography.bodyLarge)
        }
    }
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

@Preview(showBackground = true)
@Composable
fun TaskListPreview() {
    Todolistv1Theme {
        TaskList(
            taskList = listOf(
                Task(id = 1, name = "Sample Task 1", description = "This is a sample task description.", isCompleted = false),
                Task(id = 2, name = "Sample Task 2", description = "This is another task description.", isCompleted = true)
            ),
            onTaskClick = { /* Handle task click */ },
            onTaskCheckedChange = { task, isChecked ->
                // Handle checkbox state change
            },
            contentPadding = PaddingValues(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview_NoTasks() {
    HomeScreen(
        navigateToTaskEntry = {},
        navigateToTaskUpdate = {},
        viewModel = HomeViewModel(
            TasksRepositoryMock() // Replace with a mock or test repository
        )
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview_WithTasks() {
    HomeScreen(
        navigateToTaskEntry = {},
        navigateToTaskUpdate = {},
        viewModel = HomeViewModel(
            TasksRepositoryMock(listOf(
                Task(id = 1, name = "Task 1", description = "Description 1", isCompleted = false),
                Task(id = 2, name = "Task 2", description = "Description 2", isCompleted = true)
            )) // Replace with a mock or test repository
        )
    )
}



class TasksRepositoryMock(
    initialTasks: List<Task> = listOf()
) : TasksRepository {

    private val tasksFlow = MutableStateFlow(initialTasks)
    private val tasksMap = initialTasks.associateBy { it.id }.toMutableMap()

    override fun getAllTasksStream(): Flow<List<Task>> = tasksFlow

    override fun getTaskStream(id: Int): Flow<Task?> = flowOf(tasksMap[id])

    override suspend fun insertTask(task: Task) {
        tasksMap[task.id] = task
        tasksFlow.value = tasksMap.values.toList()
    }

    override suspend fun deleteTask(task: Task) {
        tasksMap.remove(task.id)
        tasksFlow.value = tasksMap.values.toList()
    }

    override suspend fun updateTask(task: Task) {
        tasksMap[task.id] = task
        tasksFlow.value = tasksMap.values.toList()
    }
}