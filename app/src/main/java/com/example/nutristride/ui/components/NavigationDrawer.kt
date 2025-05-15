package com.example.nutristride.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.nutristride.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun NutriStrideNavigationDrawer(
    drawerState: DrawerState,
    currentRoute: String,
    navController: NavController,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // App Logo/Header
                Text(
                    text = "NutriStride",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )

                Divider()
                Spacer(modifier = Modifier.height(8.dp))

                // Drawer items
                Screen.drawerItems.forEach { screen ->
                    if (screen != null) {
                        val isSelected = currentRoute == screen.route

                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(text = screen.title) },
                        selected = isSelected,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    popUpTo(Screen.Dashboard.route) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }

                            // Close the drawer
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Divider(modifier = Modifier.fillMaxWidth())

                // Version info at the bottom
                Text(
                    text = "Version 1.0",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            }
        },
        content = content
    )
}
