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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.nutristride.data.model.ActivityLevel
import com.example.nutristride.data.model.UserGoals
import com.example.nutristride.data.model.WeightGoalType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalSettingScreen(
    userGoals: UserGoals?,
    onBackClick: () -> Unit,
    onSaveGoals: (UserGoals) -> Unit
) {
    var dailyCalorieTarget by remember { mutableIntStateOf(userGoals?.dailyCalorieTarget ?: 2000) }
    var dailyStepsTarget by remember { mutableIntStateOf(userGoals?.dailyStepsTarget ?: 10000) }
    var dailyWaterTarget by remember { mutableIntStateOf(userGoals?.dailyWaterTarget ?: 2000) }
    var weeklyWorkoutTarget by remember { mutableIntStateOf(userGoals?.weeklyWorkoutTarget ?: 3) }
    
    var proteinPercentage by remember { mutableIntStateOf(userGoals?.proteinPercentage ?: 30) }
    var carbsPercentage by remember { mutableIntStateOf(userGoals?.carbsPercentage ?: 50) }
    var fatPercentage by remember { mutableIntStateOf(userGoals?.fatPercentage ?: 20) }
    
    var currentWeight by remember { mutableStateOf(userGoals?.currentWeight?.toString() ?: "") }
    var targetWeight by remember { mutableStateOf(userGoals?.targetWeight?.toString() ?: "") }
    
    var weightGoalTypeExpanded by remember { mutableStateOf(false) }
    var selectedWeightGoalType by remember { mutableStateOf(userGoals?.weightGoalType ?: WeightGoalType.MAINTAIN) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Set Goals") },
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
            // Nutrition Goals
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
                    Text(
                        text = "Daily Calorie Target",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    OutlinedTextField(
                        value = dailyCalorieTarget.toString(),
                        onValueChange = { 
                            val newValue = it.toIntOrNull() ?: dailyCalorieTarget
                            dailyCalorieTarget = newValue
                        },
                        label = { Text("Calories") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Macronutrient Distribution
                    Text(
                        text = "Macronutrient Distribution",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Protein Slider
                    MacroSlider(
                        label = "Protein",
                        value = proteinPercentage,
                        onValueChange = { proteinPercentage = it }
                    )
                    
                    // Carbs Slider
                    MacroSlider(
                        label = "Carbs",
                        value = carbsPercentage,
                        onValueChange = { carbsPercentage = it }
                    )
                    
                    // Fat Slider
                    MacroSlider(
                        label = "Fat",
                        value = fatPercentage,
                        onValueChange = { fatPercentage = it }
                    )
                    
                    // Total percentage check
                    val totalPercentage = proteinPercentage + carbsPercentage + fatPercentage
                    if (totalPercentage != 100) {
                        Text(
                            text = "Total: $totalPercentage% (should be 100%)",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Activity Goals
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
                    Text(
                        text = "Daily Steps Target",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    OutlinedTextField(
                        value = dailyStepsTarget.toString(),
                        onValueChange = { 
                            val newValue = it.toIntOrNull() ?: dailyStepsTarget
                            dailyStepsTarget = newValue
                        },
                        label = { Text("Steps") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Weekly Workout Target
                    Text(
                        text = "Weekly Workout Target",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    OutlinedTextField(
                        value = weeklyWorkoutTarget.toString(),
                        onValueChange = { 
                            val newValue = it.toIntOrNull() ?: weeklyWorkoutTarget
                            weeklyWorkoutTarget = newValue
                        },
                        label = { Text("Workouts per week") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Water Intake Goal
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
                    Text(
                        text = "Daily Water Target",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    OutlinedTextField(
                        value = dailyWaterTarget.toString(),
                        onValueChange = { 
                            val newValue = it.toIntOrNull() ?: dailyWaterTarget
                            dailyWaterTarget = newValue
                        },
                        label = { Text("ml") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Weight Goals
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
                    Text(
                        text = "Current Weight",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    OutlinedTextField(
                        value = currentWeight,
                        onValueChange = { currentWeight = it },
                        label = { Text("kg") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Weight Goal Type
                    Text(
                        text = "Weight Goal",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    ExposedDropdownMenuBox(
                        expanded = weightGoalTypeExpanded,
                        onExpandedChange = { weightGoalTypeExpanded = it },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = when (selectedWeightGoalType) {
                                WeightGoalType.LOSE -> "Lose Weight"
                                WeightGoalType.MAINTAIN -> "Maintain Weight"
                                WeightGoalType.GAIN -> "Gain Weight"
                            },
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = weightGoalTypeExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        
                        ExposedDropdownMenu(
                            expanded = weightGoalTypeExpanded,
                            onDismissRequest = { weightGoalTypeExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Lose Weight") },
                                onClick = {
                                    selectedWeightGoalType = WeightGoalType.LOSE
                                    weightGoalTypeExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Maintain Weight") },
                                onClick = {
                                    selectedWeightGoalType = WeightGoalType.MAINTAIN
                                    weightGoalTypeExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Gain Weight") },
                                onClick = {
                                    selectedWeightGoalType = WeightGoalType.GAIN
                                    weightGoalTypeExpanded = false
                                }
                            )
                        }
                    }
                    
                    // Target Weight (only if not maintaining)
                    if (selectedWeightGoalType != WeightGoalType.MAINTAIN) {
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Target Weight",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        OutlinedTextField(
                            value = targetWeight,
                            onValueChange = { targetWeight = it },
                            label = { Text("kg") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Save Button
            Button(
                onClick = {
                    val newUserGoals = UserGoals(
                        userId = userGoals?.userId ?: "user_id", // Replace with actual user ID
                        dailyCalorieTarget = dailyCalorieTarget,
                        dailyStepsTarget = dailyStepsTarget,
                        dailyWaterTarget = dailyWaterTarget,
                        weeklyWorkoutTarget = weeklyWorkoutTarget,
                        proteinPercentage = proteinPercentage,
                        carbsPercentage = carbsPercentage,
                        fatPercentage = fatPercentage,
                        currentWeight = currentWeight.toFloatOrNull(),
                        targetWeight = if (selectedWeightGoalType != WeightGoalType.MAINTAIN) targetWeight.toFloatOrNull() else null,
                        weightGoalType = selectedWeightGoalType
                    )
                    onSaveGoals(newUserGoals)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Save Goals")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MacroSlider(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge
            )
            
            Text(
                text = "$value%",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
        
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 0f..100f,
            steps = 100,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
