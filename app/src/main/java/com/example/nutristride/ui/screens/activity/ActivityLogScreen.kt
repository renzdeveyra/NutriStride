package com.example.nutristride.ui.screens.activity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Delete
import com.example.nutristride.ui.utils.getActivityIcon
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nutristride.data.model.ActivityRecord
import com.example.nutristride.data.model.ActivityType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityLogScreen(
    date: Date = Date(),
    activityRecords: List<ActivityRecord> = emptyList(),
    totalCaloriesBurned: Int = 0,
    onBackClick: () -> Unit,
    onAddActivityClick: () -> Unit = {},
    onActivityItemClick: (String) -> Unit = {},
    onDeleteActivityRecord: (ActivityRecord) -> Unit = {}
) {
    val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(date)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Activity Log") },
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
                onClick = onAddActivityClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Activity",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Date Display
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Total Calories Burned
            Text(
                text = "Total Calories Burned: $totalCaloriesBurned",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (activityRecords.isEmpty()) {
                // Empty State
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No activities logged for today",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap the + button to add an activity",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                // Activity Records
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(activityRecords) { activityRecord ->
                        ActivityRecordRow(
                            activityRecord = activityRecord,
                            onClick = { onActivityItemClick(activityRecord.id) },
                            onDelete = { onDeleteActivityRecord(activityRecord) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityRecordRow(
    activityRecord: ActivityRecord,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Activity Icon
            Icon(
                imageVector = getActivityIcon(activityRecord.type),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = activityRecord.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = timeFormat.format(activityRecord.date),
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${activityRecord.durationMinutes} min",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "•",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "${activityRecord.caloriesBurned} cal",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    if (activityRecord.distance != null) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = String.format(Locale.US, "%.2f km", activityRecord.distance),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}