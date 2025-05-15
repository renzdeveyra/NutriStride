package com.example.nutristride.ui.screens.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import com.example.nutristride.ui.utils.getActivityIcon
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nutristride.data.model.ActivityRecord
import com.example.nutristride.data.model.ActivityType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitySummaryScreen(
    activityType: ActivityType,
    durationMinutes: Int,
    caloriesBurned: Int,
    distance: Float? = null,
    steps: Int? = null,
    onBackClick: () -> Unit,
    onSaveActivity: (ActivityRecord) -> Unit
) {
    var activityName by remember {
        mutableStateOf(
            when (activityType) {
                ActivityType.WALKING -> "Walking"
                ActivityType.RUNNING -> "Running"
                ActivityType.CYCLING -> "Cycling"
                ActivityType.SWIMMING -> "Swimming"
                ActivityType.WEIGHT_TRAINING -> "Weight Training"
                ActivityType.YOGA -> "Yoga"
                ActivityType.OTHER -> "Activity"
            }
        )
    }

    val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val currentDate = Date()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Activity Summary") },
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Activity Icon
            Icon(
                imageVector = getActivityIcon(activityType),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 16.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            // Activity Name Input
            OutlinedTextField(
                value = activityName,
                onValueChange = { activityName = it },
                label = { Text("Activity Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Date and Time
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Date & Time",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = dateFormat.format(currentDate),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = timeFormat.format(currentDate),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Activity Stats
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Activity Stats",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Duration
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Duration",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = formatDuration(durationMinutes),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Calories
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Calories Burned",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = "$caloriesBurned cal",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Distance (if applicable)
                    if (distance != null) {
                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Distance",
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Text(
                                text = String.format(Locale.US, "%.2f km", distance),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Steps (if applicable)
                    if (steps != null) {
                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Steps",
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Text(
                                text = "$steps steps",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Save Button
            Button(
                onClick = {
                    val activityRecord = ActivityRecord(
                        name = activityName,
                        type = activityType,
                        durationMinutes = durationMinutes,
                        caloriesBurned = caloriesBurned,
                        distance = distance,
                        steps = steps,
                        date = currentDate
                    )
                    onSaveActivity(activityRecord)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Save Activity")
            }
        }
    }
}

fun formatDuration(minutes: Int): String {
    val hours = minutes / 60
    val mins = minutes % 60

    return if (hours > 0) {
        "$hours h $mins min"
    } else {
        "$mins min"
    }
}


