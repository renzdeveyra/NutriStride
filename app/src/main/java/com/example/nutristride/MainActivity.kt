package com.example.nutristride

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nutristride.navigation.NavGraph
import com.example.nutristride.navigation.Screen
import com.example.nutristride.ui.components.NutriStrideNavigationDrawer
import com.example.nutristride.ui.screens.DashboardScreen
import com.example.nutristride.ui.theme.NutriStrideTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutriStrideApp()
        }
    }
}

@Composable
fun NutriStrideApp() {
    NutriStrideTheme {
        val navController = rememberNavController()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        // Get current route to highlight the correct drawer item
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Dashboard.route

        NutriStrideNavigationDrawer(
            drawerState = drawerState,
            currentRoute = currentRoute,
            navController = navController
        ) {
            NavGraph(
                navController = navController
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    NutriStrideTheme {
        DashboardScreen(
            onFoodClick = {},
            onActivityClick = {},
            onWaterClick = {},
            onProfileClick = {}
        )
    }
}