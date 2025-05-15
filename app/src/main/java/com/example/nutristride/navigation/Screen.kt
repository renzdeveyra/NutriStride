package com.example.nutristride.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Dashboard : Screen(
        route = "dashboard",
        title = "Dashboard",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    object FoodLog : Screen(
        route = "food_log",
        title = "Food Log",
        selectedIcon = Icons.Filled.Restaurant,
        unselectedIcon = Icons.Outlined.Restaurant
    )

    object ActivityLog : Screen(
        route = "activity_log",
        title = "Activity Log",
        selectedIcon = Icons.Filled.DirectionsRun,
        unselectedIcon = Icons.Outlined.DirectionsRun
    )

    object Diary : Screen(
        route = "diary",
        title = "Diary",
        selectedIcon = Icons.Filled.MenuBook,
        unselectedIcon = Icons.Outlined.MenuBook
    )

    object Progress : Screen(
        route = "progress",
        title = "Progress",
        selectedIcon = Icons.Filled.ShowChart,
        unselectedIcon = Icons.Outlined.ShowChart
    )

    object Goals : Screen(
        route = "goals",
        title = "Goals",
        selectedIcon = Icons.Filled.Stars,
        unselectedIcon = Icons.Outlined.Stars
    )

    object WaterIntake : Screen(
        route = "water_intake",
        title = "Water Intake",
        selectedIcon = Icons.Filled.WaterDrop,
        unselectedIcon = Icons.Outlined.WaterDrop
    )

    object Recipes : Screen(
        route = "recipes",
        title = "Recipes",
        selectedIcon = Icons.Filled.Restaurant,
        unselectedIcon = Icons.Outlined.Restaurant
    )

    object Profile : Screen(
        route = "profile",
        title = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )

    object Settings : Screen(
        route = "settings",
        title = "Settings",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )

    companion object {
        val drawerItems = listOfNotNull(
            Dashboard,
            Diary,
            Progress,
            Goals,
            Recipes,
            Settings
        )
    }
}
