package com.example.nutristride.ui.screens.goals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nutristride.data.model.UserGoals
import com.example.nutristride.data.model.WeightGoalType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    userGoals: UserGoals? = null,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Goals") },
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
                .verticalScroll(rememberScrollState())
        ) {
            if (userGoals == null) {
                // No goals set yet
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No goals set yet",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Set your health and fitness goals to track your progress",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(onClick = { /* TODO: Navigate to goal setting screen */ }) {
                        Text("Set Goals")
                    }
                }
            } else {
                // Display current goals
                Text(
                    text = "Your Health Goals",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Nutrition Goals Card
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Nutrition Goals",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Daily Calorie Target
                        GoalItem(
                            label = "Daily Calorie Target",
                            value = "${userGoals.dailyCalorieTarget} calories"
                        )

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Macronutrient Distribution
                        Text(
                            text = "Macronutrient Distribution",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        GoalItem(
                            label = "Protein",
                            value = "${userGoals.proteinPercentage}%"
                        )

                        GoalItem(
                            label = "Carbs",
                            value = "${userGoals.carbsPercentage}%"
                        )

                        GoalItem(
                            label = "Fat",
                            value = "${userGoals.fatPercentage}%"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Activity Goals Card
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Activity Goals",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Daily Steps Target
                        GoalItem(
                            label = "Daily Steps Target",
                            value = "${userGoals.dailyStepsTarget} steps"
                        )

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Weekly Workout Target
                        GoalItem(
                            label = "Weekly Workout Target",
                            value = "${userGoals.weeklyWorkoutTarget} workouts"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Water Intake Goal Card
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Water Intake Goal",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Daily Water Target
                        GoalItem(
                            label = "Daily Water Target",
                            value = "${userGoals.dailyWaterTarget} ml"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Weight Goals Card
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Weight Goals",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Current Weight
                        userGoals.currentWeight?.let {
                            GoalItem(
                                label = "Current Weight",
                                value = "$it kg"
                            )

                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                        }

                        // Weight Goal Type
                        GoalItem(
                            label = "Weight Goal",
                            value = when (userGoals.weightGoalType) {
                                WeightGoalType.LOSE -> "Lose Weight"
                                WeightGoalType.MAINTAIN -> "Maintain Weight"
                                WeightGoalType.GAIN -> "Gain Weight"
                            }
                        )

                        // Target Weight (if applicable)
                        if (userGoals.weightGoalType != WeightGoalType.MAINTAIN && userGoals.targetWeight != null) {
                            Divider(modifier = Modifier.padding(vertical = 8.dp))

                            GoalItem(
                                label = "Target Weight",
                                value = "${userGoals.targetWeight} kg"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Edit Goals Button
                Button(
                    onClick = { /* TODO: Navigate to goal setting screen */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Edit Goals")
                }
            }
        }
    }
}

@Composable
fun GoalItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
