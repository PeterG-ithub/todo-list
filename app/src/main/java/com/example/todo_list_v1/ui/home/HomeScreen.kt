package com.example.todo_list_v1.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_list_v1.R
import com.example.todo_list_v1.data.category.Category
import com.example.todo_list_v1.data.category.CategoryRepository
import com.example.todo_list_v1.data.task.Task
import com.example.todo_list_v1.data.task.TasksRepository
import com.example.todo_list_v1.ui.AppViewModelProvider
import com.example.todo_list_v1.ui.category.CategoryEntryModal
import com.example.todo_list_v1.ui.category.CategoryUiState
import com.example.todo_list_v1.ui.category.CategoryViewModel
import com.example.todo_list_v1.ui.navigation.NavigationDestination
import com.example.todo_list_v1.ui.task.item.TaskItem
import com.example.todo_list_v1.ui.theme.Todolistv1Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToTaskEntry: (Category?) -> Unit,
    navigateToTaskUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val homeUiState by homeViewModel.homeUiState.collectAsState()
    val selectedCategoryId by homeViewModel.selectedCategoryId.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // State for managing the visibility of the category entry modal
    val showCategoryEntryModal = remember { mutableStateOf(false) }
    var showDropdownMenu by remember { mutableStateOf(false) }
    val selectedCategory = remember { mutableStateOf<Category?>(null) }

    val filteredTaskList by homeViewModel.filteredTasks.collectAsState()

    // Update selected category state based on the ViewModel
    LaunchedEffect(selectedCategoryId) {
        selectedCategory.value = homeUiState.categoryList.find { it.id == selectedCategoryId }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    HomeTopAppBar(
                        categories = homeUiState.categoryList,
                        onCategoryClick = { category ->
                            selectedCategory.value = category
                            homeViewModel.selectCategory(category.id) // Update the category in ViewModel
                        },
                        onAllClick = {
                            selectedCategory.value = null
                            homeViewModel.selectCategory(null) // Show all tasks
                        },
                        onCategoryEntryClick = { showCategoryEntryModal.value = true },
                        selectedCategory = selectedCategory.value,
                        modifier = modifier
                    )
                },
                actions = {
                    IconButton(onClick = { showDropdownMenu = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = stringResource(R.string.more_options))
                    }

                    MoreOptionDropdown(
                        onDismissRequest = { showDropdownMenu = false },
                        showOptionMenu = showDropdownMenu,
                        onOptionSelected = { option ->
                            when (option) {
                                "Add" -> {
                                    showDropdownMenu = false
                                    showCategoryEntryModal.value = true
                                }
                                "Manage" -> {
                                    /* TODO: Maybe navigate to a new category screen */
                                }
                                "Delete" -> {
                                    selectedCategory.value?.let { category ->
                                        homeViewModel.deleteCategory(category)
                                    }
                                    selectedCategory.value = null
                                    homeViewModel.selectCategory(null)
                                }
                                "Sort" -> {
                                    /* TODO */
                                }
                                else -> {
                                    showDropdownMenu = false
                                }
                            }
                        }
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToTaskEntry(selectedCategory.value) },
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
            taskList = filteredTaskList,
            onTaskClick = navigateToTaskUpdate,
            onTaskDeleteClick = { task ->
                homeViewModel.deleteTask(task)
            },
            onTaskCheckedChange = { task, isChecked ->
                homeViewModel.updateTaskCompletion(task, isChecked)
            },
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )

        if (showCategoryEntryModal.value) {
            CategoryEntryModal(
                onDismiss = { showCategoryEntryModal.value = false },
                onConfirm = { showCategoryEntryModal.value = false }
            )
        }
    }
}


@Composable
private fun HomeTopAppBar(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit,
    onAllClick: () -> Unit, // Special handler for the "All" option
    onCategoryEntryClick: () -> Unit,
    selectedCategory: Category?, // Pass the selected category
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
        ) {
            item {
                val isSelected = selectedCategory == null // "All" is selected when no category is
                Text(
                    text = "All",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                        .clickable { onAllClick() }, // Call onAllClick when "All" is clicked
                    textAlign = TextAlign.Center
                )
            }
            items(categories) { category ->
                val isSelected = selectedCategory == category
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.padding_small),
                            vertical = dimensionResource(id = R.dimen.padding_tiny)
                        )
                        .clickable { onCategoryClick(category) }
                        .padding(horizontal = 8.dp, vertical = 0.dp),
                    textAlign = TextAlign.Center
                )
            }
            item {
                Text(
                    text = "Add Category +",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(8.dp),
                        )
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.padding_small),
                            vertical = dimensionResource(id = R.dimen.padding_tiny)
                        )
                        .clickable { onCategoryEntryClick() }
                        .padding(horizontal = 8.dp, vertical = 0.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun MoreOptionDropdown(
    onDismissRequest: () -> Unit,
    showOptionMenu: Boolean,
    onOptionSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = showOptionMenu,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        offset = DpOffset(x = (-8).dp, y = 0.dp)
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = "Add Category",
                    color = MaterialTheme.colorScheme.primary,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector =  Icons.Default.Add,
                    contentDescription = null
                )
            },
            onClick = {
                onOptionSelected("Add")
                onDismissRequest()
            }
        )
        DropdownMenuItem(
            text = {
                Text(
                    text = "Manage Categories",
                )
            },
            leadingIcon = {
                Icon(
                    imageVector =  Icons.Default.Edit,
                    contentDescription = null
                )
            },
            onClick = {
                onOptionSelected("Manage")
                onDismissRequest()
            }
        )
        DropdownMenuItem(
            text = {
                Text(
                    text = "Delete Category",
                    color = MaterialTheme.colorScheme.error
                )
            },
            leadingIcon = {
                Icon(
                    imageVector =  Icons.Filled.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            onClick = {
                onOptionSelected("Delete")
                onDismissRequest()
            }
        )
        DropdownMenuItem(
            text = {
                Text(
                    text = "Sort task by",
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.sort),
                    contentDescription = null
                )
            },
            onClick = {
                onOptionSelected("Sort")
                onDismissRequest()
            }
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
    onTaskDeleteClick: (Task) -> Unit = { }
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
                onTaskDeleteClick = onTaskDeleteClick,
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
    modifier: Modifier = Modifier,
    onTaskDeleteClick: (Task) -> Unit = { }
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
                onDeleteClick = { onTaskDeleteClick(task) },
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_tiny))
                    .clickable { onTaskClick(task) }
            )
        }
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
        homeViewModel = HomeViewModel(
            tasksRepository = TasksRepositoryMock(), // Replace with a mock or test repository
            categoryRepository = CategoryRepositoryMock() // Replace with a mock or test repository
        )
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview_WithTasks() {
    HomeScreen(
        navigateToTaskEntry = {},
        navigateToTaskUpdate = {},
        homeViewModel = HomeViewModel(
            tasksRepository = TasksRepositoryMock(listOf(
                Task(id = 1, name = "Task 1", description = "Description 1", isCompleted = false),
                Task(id = 2, name = "Task 2", description = "Description 2", isCompleted = true)
            )), // Replace with a mock or test repository
            categoryRepository = CategoryRepositoryMock(listOf(
                Category(id = 1, name = "Work", description = "Work tasks", color = "#FF5733"),
                Category(id = 2, name = "Personal", description = "Personal tasks", color = "#33FF57")
            )) // Replace with a mock or test repository
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewHomeTopAppBar() {
    val sampleCategories = List(10) { index ->
        Category(
            id = index + 1,
            name = "Category ${index + 1}",
            description = "Description ${index + 1}",
            color = null,
            icon = null,
            createdDate = System.currentTimeMillis(),
            lastUpdated = System.currentTimeMillis(),
            isArchived = false
        )
    }

    HomeTopAppBar(
        categories = sampleCategories,
        onCategoryClick = { category ->
            // Handle category click here
        },
        onCategoryEntryClick = {
            // Handle add category click here
        },
        selectedCategory = null,
        onAllClick = {}
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

    override fun getTasksByCategoryStream(categoryId: Int): Flow<List<Task>> {
        TODO("Not yet implemented")
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


class CategoryRepositoryMock(
    private val initialCategories: List<Category> = emptyList()
) : CategoryRepository {

    private val categoriesFlow = MutableStateFlow(initialCategories)

    override fun getAllCategoriesStream(): Flow<List<Category>> = categoriesFlow

    override fun getCategoryStream(id: Int): Flow<Category?> {
        return categoriesFlow.map { categories ->
            categories.find { it.id == id }
        }
    }

    override suspend fun insertCategory(category: Category) {
        // Add category to the list and update flow
        val updatedList = categoriesFlow.value.toMutableList().apply {
            add(category)
        }
        categoriesFlow.value = updatedList
    }

    override suspend fun updateCategory(category: Category) {
        // Update category in the list and update flow
        val updatedList = categoriesFlow.value.map {
            if (it.id == category.id) category else it
        }
        categoriesFlow.value = updatedList
    }

    override suspend fun deleteCategory(category: Category) {
        // Remove category from the list and update flow
        val updatedList = categoriesFlow.value.filter { it.id != category.id }
        categoriesFlow.value = updatedList
    }

    override suspend fun archiveCategory(categoryId: Int) {
        // Archive category by updating its state and update flow
        val updatedList = categoriesFlow.value.map {
            if (it.id == categoryId) it.copy(isArchived = true) else it
        }
        categoriesFlow.value = updatedList
    }
}