package com.example.nutristride.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nutristride.ui.screens.DashboardScreen
import com.example.nutristride.ui.screens.food.FoodLogScreen
import com.example.nutristride.ui.screens.food.FoodSearchScreen
import com.example.nutristride.ui.screens.food.FoodDetailsScreen
import com.example.nutristride.ui.screens.food.ManualFoodEntryScreen
import com.example.nutristride.ui.screens.food.FoodDiaryScreen
import com.example.nutristride.ui.screens.activity.ActivityLogScreen
import com.example.nutristride.ui.screens.activity.ActivitySelectionScreen
import com.example.nutristride.ui.screens.activity.ActivityTrackingScreen
import com.example.nutristride.ui.screens.activity.ActivitySummaryScreen
import com.example.nutristride.ui.screens.diary.DiaryScreen
import com.example.nutristride.ui.screens.progress.ProgressScreen
import com.example.nutristride.ui.screens.goals.GoalsScreen
import com.example.nutristride.ui.screens.goals.GoalSettingScreen
import com.example.nutristride.ui.screens.water.WaterIntakeScreen
import com.example.nutristride.ui.screens.recipes.RecipesScreen
import com.example.nutristride.ui.screens.profile.ProfileScreen
import com.example.nutristride.ui.screens.settings.SettingsScreen
import com.example.nutristride.data.model.ActivityType
import com.example.nutristride.ui.screens.auth.LoginScreen
import com.example.nutristride.ui.viewmodels.AuthViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.nutristride.ui.viewmodels.AuthState

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = when (authState) {
            is AuthState.Authenticated, is AuthState.GuestUser -> Screen.Dashboard.route
            else -> "login"
        }
    ) {
        // Add login screen
        composable(route = "login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                onFoodClick = { navController.navigate(Screen.FoodLog.route) },
                onActivityClick = { navController.navigate(Screen.ActivityLog.route) },
                onWaterClick = { navController.navigate(Screen.WaterIntake.route) },
                onProfileClick = { navController.navigate(Screen.Profile.route) }
            )
        }

        // Food Logging Flow
        composable(route = Screen.FoodLog.route) {
            FoodLogScreen(
                onBackClick = { navController.popBackStack() },
                onAddFoodClick = { navController.navigate("food_search") },
                onManualEntryClick = { navController.navigate("food_manual_entry") },
                onFoodItemClick = { foodId -> navController.navigate("food_details/$foodId") },
                onViewDiaryClick = { navController.navigate("food_diary") }
            )
        }

        composable(route = "food_search") {
            FoodSearchScreen(
                onBackClick = { navController.popBackStack() },
                onFoodItemClick = { foodId -> navController.navigate("food_details/$foodId") },
                onAddManualClick = { navController.navigate("food_manual_entry") },
                onScanBarcodeClick = { /* TODO: Implement barcode scanning */ }
            )
        }

        composable(route = "food_details/{foodId}") { backStackEntry ->
            val foodId = backStackEntry.arguments?.getString("foodId") ?: ""
            FoodDetailsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = "food_manual_entry") {
            ManualFoodEntryScreen(
                onBackClick = { navController.popBackStack() },
                onSaveFood = { foodItem ->
                    // Navigate back to the food log screen after saving
                    navController.popBackStack(Screen.FoodLog.route, false)
                }
            )
        }

        composable(route = "food_diary") {
            FoodDiaryScreen(
                onBackClick = { navController.popBackStack() },
                onAddFoodClick = { navController.navigate("food_search") },
                onFoodItemClick = { foodId -> navController.navigate("food_details/$foodId") }
            )
        }

        // Activity Logging Flow
        composable(route = Screen.ActivityLog.route) {
            ActivityLogScreen(
                onBackClick = { navController.popBackStack() },
                onAddActivityClick = { navController.navigate("activity_selection") },
                onActivityItemClick = { activityId -> navController.navigate("activity_details/$activityId") }
            )
        }

        composable(route = "activity_selection") {
            ActivitySelectionScreen(
                onBackClick = { navController.popBackStack() },
                onActivityTypeClick = { activityType ->
                    navController.navigate("activity_tracking/${activityType.name}")
                },
                onActivityItemClick = { activityId ->
                    navController.navigate("activity_details/$activityId")
                },
                onAddManualClick = { /* TODO: Implement manual activity entry */ }
            )
        }

        composable(route = "activity_tracking/{activityType}") { backStackEntry ->
            val activityTypeName = backStackEntry.arguments?.getString("activityType") ?: ActivityType.WALKING.name
            val activityType = ActivityType.valueOf(activityTypeName)

            ActivityTrackingScreen(
                activityType = activityType,
                onBackClick = { navController.popBackStack() },
                onFinishActivity = { durationMinutes, caloriesBurned, distance, steps ->
                    navController.navigate("activity_summary/${activityType.name}/$durationMinutes/$caloriesBurned?distance=${distance ?: 0f}&steps=${steps ?: 0}")
                }
            )
        }

        composable(
            route = "activity_summary/{activityType}/{durationMinutes}/{caloriesBurned}?distance={distance}&steps={steps}"
        ) { backStackEntry ->
            val activityTypeName = backStackEntry.arguments?.getString("activityType") ?: ActivityType.WALKING.name
            val activityType = ActivityType.valueOf(activityTypeName)
            val durationMinutes = backStackEntry.arguments?.getString("durationMinutes")?.toIntOrNull() ?: 0
            val caloriesBurned = backStackEntry.arguments?.getString("caloriesBurned")?.toIntOrNull() ?: 0
            val distance = backStackEntry.arguments?.getString("distance")?.toFloatOrNull()
            val steps = backStackEntry.arguments?.getString("steps")?.toIntOrNull()

            ActivitySummaryScreen(
                activityType = activityType,
                durationMinutes = durationMinutes,
                caloriesBurned = caloriesBurned,
                distance = distance,
                steps = steps,
                onBackClick = { navController.popBackStack() },
                onSaveActivity = {
                    navController.popBackStack(Screen.ActivityLog.route, false)
                }
            )
        }

        // Other Screens
        composable(route = Screen.Diary.route) {
            DiaryScreen(onBackClick = { navController.popBackStack() })
        }

        composable(route = Screen.Progress.route) {
            ProgressScreen(onBackClick = { navController.popBackStack() })
        }

        // Goals Screens
        composable(route = Screen.Goals.route) {
            GoalsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = "goal_setting") {
            GoalSettingScreen(
                userGoals = null, // TODO: Load user goals from repository
                onBackClick = { navController.popBackStack() },
                onSaveGoals = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Screen.WaterIntake.route) {
            WaterIntakeScreen(onBackClick = { navController.popBackStack() })
        }

        composable(route = Screen.Recipes.route) {
            RecipesScreen(onBackClick = { navController.popBackStack() })
        }

        composable(route = Screen.Profile.route) {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onSignOut = {
                    authViewModel.signOut()
                    navController.navigate("login") {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                },
                isGuestUser = authState is AuthState.GuestUser
            )
        }

        composable(route = Screen.Settings.route) {
            SettingsScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
