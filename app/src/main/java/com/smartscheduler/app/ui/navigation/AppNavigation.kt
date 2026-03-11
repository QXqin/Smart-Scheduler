package com.smartscheduler.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.smartscheduler.app.ui.calendar.CalendarScreen
import com.smartscheduler.app.ui.todo.TodoScreen
import com.smartscheduler.app.ui.dashboard.DashboardScreen
import com.smartscheduler.app.ui.settings.SettingsScreen
import com.smartscheduler.app.ui.statistics.StatisticsScreen
import com.smartscheduler.app.ui.focus.FocusTimerScreen

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Dashboard : Screen("dashboard", "看板", Icons.Default.Dashboard)
    data object Calendar : Screen("calendar", "本周", Icons.Default.CalendarMonth)
    data object Focus : Screen("focus", "专注", Icons.Default.Timer)
    data object Todos : Screen("todos", "待办", Icons.Default.ChecklistRtl)
    data object Statistics : Screen("statistics", "统计", Icons.Default.BarChart)
    data object Settings : Screen("settings", "设置", Icons.Default.Settings)
}

private val screens = listOf(Screen.Dashboard, Screen.Calendar, Screen.Focus, Screen.Todos, Screen.Statistics, Screen.Settings)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) { 
                DashboardScreen(
                    onNavigateToStatistics = {
                        navController.navigate(Screen.Statistics.route)
                    }
                ) 
            }
            composable(Screen.Calendar.route) { CalendarScreen() }
            composable(Screen.Focus.route) { FocusTimerScreen() }
            composable(Screen.Todos.route) { TodoScreen() }
            composable(Screen.Statistics.route) { StatisticsScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
        }
    }
}
