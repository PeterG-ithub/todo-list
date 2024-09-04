package com.example.todo_list_v1.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todo_list_v1.ui.home.HomeDestination
import com.example.todo_list_v1.ui.home.HomeScreen
import com.example.todo_list_v1.ui.task.TaskDetailsDestination
import com.example.todo_list_v1.ui.task.TaskDetailsScreen
import com.example.todo_list_v1.ui.task.TaskEditDestination
import com.example.todo_list_v1.ui.task.TaskEditScreen
import com.example.todo_list_v1.ui.task.TaskEntryDestination
import com.example.todo_list_v1.ui.task.TaskEntryScreen

@Composable
fun TodoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,  // Update this if your start destination is different
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToTaskEntry = { navController.navigate(TaskEntryDestination.route) },
                navigateToTaskUpdate = {
                    navController.navigate("${TaskDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = TaskEntryDestination.route) {
            TaskEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
//        composable(
//            route = TaskDetailsDestination.routeWithArgs,
//            arguments = listOf(navArgument(TaskDetailsDestination.taskIdArg) {
//                type = NavType.IntType
//            })
//        ) {
//            TaskDetailsScreen(
//                navigateToEditTask = { navController.navigate("${TaskEditDestination.route}/$it") },
//                navigateBack = { navController.navigateUp() }
//            )
//        }
//        composable(
//            route = TaskEditDestination.routeWithArgs,
//            arguments = listOf(navArgument(TaskEditDestination.taskIdArg) {
//                type = NavType.IntType
//            })
//        ) {
//            TaskEditScreen(
//                navigateBack = { navController.popBackStack() },
//                onNavigateUp = { navController.navigateUp() }
//            )
//        }
    }
}


