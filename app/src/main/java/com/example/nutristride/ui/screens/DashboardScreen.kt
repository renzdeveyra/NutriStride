package com.example.nutristride.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nutristride.ui.components.*

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    onFoodClick: () -> Unit = {},
    onActivityClick: () -> Unit = {},
    onWaterClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header with logo and profile
        AppHeader(onProfileClick = onProfileClick)

        Spacer(modifier = Modifier.height(16.dp))

        // Main Steps Progress
        CircularProgressIndicator(
            progress = 0.42f, // 42% progress (4,186 / 10,000)
            currentValue = 4186,
            maxValue = 10000,
            label = "Steps"
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Calories Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Calories Consumed
            SmallCircularProgressIndicator(
                progress = 0.5f,
                currentValue = 50,
                maxValue = 100,
                label = "Calories\nConsumed",
                modifier = Modifier.weight(1f)
            )

            // Calories Burned
            SmallCircularProgressIndicator(
                progress = 0.5f,
                currentValue = 50,
                maxValue = 100,
                label = "Calories\nBurned",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Action Buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Food Logging Button
            ActionButton(
                text = "Food",
                onClick = onFoodClick
            )

            // Activity Logging Button
            ActionButton(
                text = "Activity",
                onClick = onActivityClick
            )

            // Water Intake Button
            ActionButton(
                text = "Water",
                onClick = onWaterClick
            )
        }
    }
}
