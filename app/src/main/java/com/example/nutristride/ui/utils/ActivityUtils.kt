package com.example.nutristride.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.nutristride.data.model.ActivityType

@Composable
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
