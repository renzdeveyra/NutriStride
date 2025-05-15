package com.example.nutristride.ui.screens.activity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.SelfImprovement
import com.example.nutristride.ui.utils.getActivityIcon
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nutristride.data.model.ActivityRecord
import com.example.nutristride.data.model.ActivityType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitySelectionScreen(
    onBackClick: () -> Unit,
    onActivityTypeClick: (ActivityType) -> Unit,
    onActivityItemClick: (String) -> Unit,
    onAddManualClick: () -> Unit,
    recentActivities: List<ActivityRecord> = emptyList()
) {
    var searchQuery by remember { mutableStateOf("") }
    var activeTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Activities", "Recent")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Choose Activity") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddManualClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Activity Manually",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Bar
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = { /* Perform search */ },
                active = false,
                onActiveChange = { },
                placeholder = { Text("Search for activity...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Search suggestions would go here
            }

            // Tabs
            TabRow(selectedTabIndex = activeTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = activeTabIndex == index,
                        onClick = { activeTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            // Content based on selected tab
            when (activeTabIndex) {
                0 -> ActivityTypesList(onActivityTypeClick = onActivityTypeClick)
                1 -> RecentActivitiesList(
                    recentActivities = recentActivities,
                    onActivityItemClick = onActivityItemClick
                )
            }
        }
    }
}

@Composable
fun ActivityTypesList(
    onActivityTypeClick: (ActivityType) -> Unit
) {
    val activityTypes = listOf(
        ActivityTypeItem(ActivityType.WALKING, "Walking", Icons.Default.DirectionsWalk),
        ActivityTypeItem(ActivityType.RUNNING, "Running", Icons.Default.DirectionsRun),
        ActivityTypeItem(ActivityType.CYCLING, "Cycling", Icons.Default.DirectionsBike),
        ActivityTypeItem(ActivityType.SWIMMING, "Swimming", Icons.Default.Pool),
        ActivityTypeItem(ActivityType.WEIGHT_TRAINING, "Weight Training", Icons.Default.FitnessCenter),
        ActivityTypeItem(ActivityType.YOGA, "Yoga", Icons.Default.SelfImprovement),
        ActivityTypeItem(ActivityType.OTHER, "Other Activity", Icons.Default.Add)
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(activityTypes) { activityType ->
            ActivityTypeRow(
                activityType = activityType,
                onClick = { onActivityTypeClick(activityType.type) }
            )
        }
    }
}

@Composable
fun ActivityTypeRow(
    activityType: ActivityTypeItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = activityType.icon,
                contentDescription = activityType.name,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = activityType.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun RecentActivitiesList(
    recentActivities: List<ActivityRecord>,
    onActivityItemClick: (String) -> Unit
) {
    if (recentActivities.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "No recent activities",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Activities you log will appear here",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else {
        LazyColumn {
            items(recentActivities) { activity ->
                ActivityItemRow(
                    activity = activity,
                    onClick = { onActivityItemClick(activity.id) }
                )
            }
        }
    }
}

@Composable
fun ActivityItemRow(
    activity: ActivityRecord,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = getActivityIcon(activity.type),
                contentDescription = activity.name,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${activity.durationMinutes} min • ${activity.caloriesBurned} cal",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

data class ActivityTypeItem(
    val type: ActivityType,
    val name: String,
    val icon: ImageVector
)
