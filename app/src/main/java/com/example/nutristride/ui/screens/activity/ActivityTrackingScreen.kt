package com.example.nutristride.ui.screens.activity

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutristride.data.model.ActivityType
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityTrackingScreen(
    activityType: ActivityType,
    onBackClick: () -> Unit,
    onFinishActivity: (durationMinutes: Int, caloriesBurned: Int, distance: Float?, steps: Int?) -> Unit
) {
    var isTracking by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var elapsedTimeSeconds by remember { mutableIntStateOf(0) }
    var caloriesBurned by remember { mutableIntStateOf(0) }
    var distance by remember { mutableStateOf(0f) } // in km
    var steps by remember { mutableIntStateOf(0) }

    // Timer effect
    LaunchedEffect(isTracking, isPaused) {
        while (isTracking && !isPaused) {
            delay(1000)
            elapsedTimeSeconds++

            // Simulate calorie burn (very simplified)
            val caloriesPerMinute = when (activityType) {
                ActivityType.WALKING -> 4
                ActivityType.RUNNING -> 10
                ActivityType.CYCLING -> 8
                ActivityType.SWIMMING -> 9
                ActivityType.WEIGHT_TRAINING -> 6
                ActivityType.YOGA -> 3
                ActivityType.OTHER -> 5
            }
            caloriesBurned = (elapsedTimeSeconds / 60.0 * caloriesPerMinute).roundToInt()

            // Simulate distance (for applicable activities)
            if (activityType in listOf(ActivityType.WALKING, ActivityType.RUNNING, ActivityType.CYCLING)) {
                val speedKmPerHour = when (activityType) {
                    ActivityType.WALKING -> 5.0f
                    ActivityType.RUNNING -> 10.0f
                    ActivityType.CYCLING -> 20.0f
                    else -> 0f
                }
                distance = elapsedTimeSeconds / 3600f * speedKmPerHour
            }

            // Simulate steps (for walking and running)
            if (activityType in listOf(ActivityType.WALKING, ActivityType.RUNNING)) {
                val stepsPerMinute = when (activityType) {
                    ActivityType.WALKING -> 100
                    ActivityType.RUNNING -> 160
                    else -> 0
                }
                steps = (elapsedTimeSeconds / 60.0 * stepsPerMinute).roundToInt()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (activityType) {
                            ActivityType.WALKING -> "Walking"
                            ActivityType.RUNNING -> "Running"
                            ActivityType.CYCLING -> "Cycling"
                            ActivityType.SWIMMING -> "Swimming"
                            ActivityType.WEIGHT_TRAINING -> "Weight Training"
                            ActivityType.YOGA -> "Yoga"
                            ActivityType.OTHER -> "Activity"
                        }
                    )
                },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Activity Icon
            Box(
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getActivityIcon(activityType),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            // Timer Display
            Text(
                text = formatTime(elapsedTimeSeconds),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Stats Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Calories Card
                StatCard(
                    title = "Calories",
                    value = caloriesBurned.toString(),
                    unit = "cal",
                    modifier = Modifier.weight(1f)
                )

                // Distance Card (if applicable)
                if (activityType in listOf(ActivityType.WALKING, ActivityType.RUNNING, ActivityType.CYCLING)) {
                    StatCard(
                        title = "Distance",
                        value = String.format(Locale.US, "%.2f", distance),
                        unit = "km",
                        modifier = Modifier.weight(1f)
                    )
                }

                // Steps Card (if applicable)
                if (activityType in listOf(ActivityType.WALKING, ActivityType.RUNNING)) {
                    StatCard(
                        title = "Steps",
                        value = steps.toString(),
                        unit = "",
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Control Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Start/Pause Button
                FloatingActionButton(
                    onClick = {
                        if (!isTracking) {
                            isTracking = true
                            isPaused = false
                        } else {
                            isPaused = !isPaused
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    shape = CircleShape,
                    modifier = Modifier.size(72.dp)
                ) {
                    Icon(
                        imageVector = when {
                            !isTracking || isPaused -> Icons.Default.PlayArrow
                            else -> Icons.Default.Pause
                        },
                        contentDescription = if (!isTracking || isPaused) "Start" else "Pause",
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                // Stop Button (only visible when tracking)
                if (isTracking) {
                    FloatingActionButton(
                        onClick = {
                            isTracking = false
                            isPaused = false

                            // Calculate final values
                            val durationMinutes = elapsedTimeSeconds / 60
                            val finalDistance = if (activityType in listOf(ActivityType.WALKING, ActivityType.RUNNING, ActivityType.CYCLING)) distance else null
                            val finalSteps = if (activityType in listOf(ActivityType.WALKING, ActivityType.RUNNING)) steps else null

                            onFinishActivity(durationMinutes, caloriesBurned, finalDistance, finalSteps)
                        },
                        containerColor = MaterialTheme.colorScheme.error,
                        shape = CircleShape,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Stop",
                            tint = MaterialTheme.colorScheme.onError
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Finish Button (alternative to stop button)
            if (isTracking) {
                Button(
                    onClick = {
                        isTracking = false
                        isPaused = false

                        // Calculate final values
                        val durationMinutes = elapsedTimeSeconds / 60
                        val finalDistance = if (activityType in listOf(ActivityType.WALKING, ActivityType.RUNNING, ActivityType.CYCLING)) distance else null
                        val finalSteps = if (activityType in listOf(ActivityType.WALKING, ActivityType.RUNNING)) steps else null

                        onFinishActivity(durationMinutes, caloriesBurned, finalDistance, finalSteps)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finish Activity")
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            if (unit.isNotEmpty()) {
                Text(
                    text = unit,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

fun formatTime(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60

    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, secs)
    } else {
        String.format("%02d:%02d", minutes, secs)
    }
}

fun getActivityIcon(activityType: ActivityType): ImageVector {
    return when (activityType) {
        ActivityType.WALKING -> Icons.Default.DirectionsWalk
        ActivityType.RUNNING -> Icons.Default.DirectionsRun
        ActivityType.CYCLING -> Icons.Default.DirectionsBike
        ActivityType.SWIMMING -> Icons.Default.Pool
        ActivityType.WEIGHT_TRAINING -> Icons.Default.FitnessCenter
        ActivityType.YOGA -> Icons.Default.SelfImprovement
        ActivityType.OTHER -> Icons.Default.DirectionsRun
    }
}
