package com.example.nutristride.ui.screens.food

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.model.MealType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailsScreen(
    foodItem: FoodItem?,
    onBackClick: () -> Unit,
    onLogFoodClick: (FoodItem, MealType, Float) -> Unit,
    onToggleFavorite: (FoodItem) -> Unit
) {
    var selectedMealType by remember { mutableStateOf(MealType.BREAKFAST) }
    var servingSize by remember { mutableStateOf("1") }
    var expanded by remember { mutableStateOf(false) }
    val servingSizeOptions = listOf("0.5", "1", "1.5", "2", "2.5", "3")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Food Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (foodItem != null) {
                        IconButton(onClick = { onToggleFavorite(foodItem) }) {
                            Icon(
                                imageVector = if (foodItem.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Toggle Favorite"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        if (foodItem == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Food item not found",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Food Name and Brand
                Text(
                    text = foodItem.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                if (foodItem.brand != null) {
                    Text(
                        text = foodItem.brand,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Nutritional Information Card
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Nutritional Information",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Per serving info
                        Text(
                            text = "Per serving (${foodItem.servingSize} ${foodItem.servingUnit})",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Calories
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Calories",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${foodItem.calories}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        
                        // Macronutrients
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Protein",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "${foodItem.protein}g",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Carbohydrates",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "${foodItem.carbs}g",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Fat",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "${foodItem.fat}g",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Serving Size Dropdown
                Text(
                    text = "Serving Size",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = servingSize,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        servingSizeOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text("$option serving${if (option != "1") "s" else ""}") },
                                onClick = {
                                    servingSize = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Meal Type Selection
                Text(
                    text = "Add to Meal",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = selectedMealType == MealType.BREAKFAST,
                        onClick = { selectedMealType = MealType.BREAKFAST },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 4)
                    ) {
                        Text("Breakfast")
                    }
                    SegmentedButton(
                        selected = selectedMealType == MealType.LUNCH,
                        onClick = { selectedMealType = MealType.LUNCH },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 4)
                    ) {
                        Text("Lunch")
                    }
                    SegmentedButton(
                        selected = selectedMealType == MealType.DINNER,
                        onClick = { selectedMealType = MealType.DINNER },
                        shape = SegmentedButtonDefaults.itemShape(index = 2, count = 4)
                    ) {
                        Text("Dinner")
                    }
                    SegmentedButton(
                        selected = selectedMealType == MealType.SNACK,
                        onClick = { selectedMealType = MealType.SNACK },
                        shape = SegmentedButtonDefaults.itemShape(index = 3, count = 4)
                    ) {
                        Text("Snack")
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Log Food Button
                Button(
                    onClick = {
                        onLogFoodClick(
                            foodItem,
                            selectedMealType,
                            servingSize.toFloatOrNull() ?: 1f
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Log Food")
                }
            }
        }
    }
}
