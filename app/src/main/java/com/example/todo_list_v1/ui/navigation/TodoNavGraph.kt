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
                navigateToTaskEntry = { selectedCategory ->
                    // Pass selectedCategory ID if it's not null
                    navController.navigate(
                        route = "${TaskEntryDestination.route}/${selectedCategory?.id ?: -1}"
                    )
                },
                navigateToTaskUpdate = {
                    navController.navigate("${TaskEditDestination.route}/${it}")
                },
                navigateToCompletedTask =  {

                },
            )
        }
        composable(
            route = "${TaskEntryDestination.route}/{categoryId}",
            arguments = listOf(navArgument("categoryId") {
                type = NavType.IntType
                defaultValue = -1 // Provide a default value to handle null
            })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getInt("categoryId")
            TaskEntryScreen(
                categoryId = categoryId,
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = TaskEditDestination.routeWithArgs,
            arguments = listOf(navArgument(TaskEditDestination.taskIdArg) {
                type = NavType.IntType
            })
        ) {
            TaskEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}


