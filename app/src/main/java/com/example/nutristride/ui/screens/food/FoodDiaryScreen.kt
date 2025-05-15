package com.example.nutristride.ui.screens.food

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.model.MealType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDiaryScreen(
    date: Date = Date(),
    foodItems: Map<MealType, List<FoodItem>>,
    totalCalories: Int,
    onBackClick: () -> Unit,
    onAddFoodClick: () -> Unit,
    onFoodItemClick: (String) -> Unit,
    onDeleteFoodItem: (FoodItem) -> Unit
) {
    val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(date)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Food Diary") },
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
                onClick = onAddFoodClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Food",
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
            
            // Total Calories
            Text(
                text = "Total Calories: $totalCalories",
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (foodItems.isEmpty() || foodItems.all { it.value.isEmpty() }) {
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
                            text = "No food logged for today",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap the + button to add food",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                // Food Items by Meal Type
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    // Breakfast
                    item {
                        MealTypeHeader(
                            mealType = MealType.BREAKFAST,
                            totalCalories = foodItems[MealType.BREAKFAST]?.sumOf { it.calories } ?: 0
                        )
                    }
                    
                    items(foodItems[MealType.BREAKFAST] ?: emptyList()) { foodItem ->
                        FoodItemRow(
                            foodItem = foodItem,
                            onClick = { onFoodItemClick(foodItem.id) },
                            onDelete = { onDeleteFoodItem(foodItem) }
                        )
                    }
                    
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    
                    // Lunch
                    item {
                        MealTypeHeader(
                            mealType = MealType.LUNCH,
                            totalCalories = foodItems[MealType.LUNCH]?.sumOf { it.calories } ?: 0
                        )
                    }
                    
                    items(foodItems[MealType.LUNCH] ?: emptyList()) { foodItem ->
                        FoodItemRow(
                            foodItem = foodItem,
                            onClick = { onFoodItemClick(foodItem.id) },
                            onDelete = { onDeleteFoodItem(foodItem) }
                        )
                    }
                    
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    
                    // Dinner
                    item {
                        MealTypeHeader(
                            mealType = MealType.DINNER,
                            totalCalories = foodItems[MealType.DINNER]?.sumOf { it.calories } ?: 0
                        )
                    }
                    
                    items(foodItems[MealType.DINNER] ?: emptyList()) { foodItem ->
                        FoodItemRow(
                            foodItem = foodItem,
                            onClick = { onFoodItemClick(foodItem.id) },
                            onDelete = { onDeleteFoodItem(foodItem) }
                        )
                    }
                    
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    
                    // Snacks
                    item {
                        MealTypeHeader(
                            mealType = MealType.SNACK,
                            totalCalories = foodItems[MealType.SNACK]?.sumOf { it.calories } ?: 0
                        )
                    }
                    
                    items(foodItems[MealType.SNACK] ?: emptyList()) { foodItem ->
                        FoodItemRow(
                            foodItem = foodItem,
                            onClick = { onFoodItemClick(foodItem.id) },
                            onDelete = { onDeleteFoodItem(foodItem) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MealTypeHeader(
    mealType: MealType,
    totalCalories: Int
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
                text = when (mealType) {
                    MealType.BREAKFAST -> "Breakfast"
                    MealType.LUNCH -> "Lunch"
                    MealType.DINNER -> "Dinner"
                    MealType.SNACK -> "Snacks"
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "$totalCalories cal",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    }
}

@Composable
fun FoodItemRow(
    foodItem: FoodItem,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = foodItem.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                
                if (foodItem.brand != null) {
                    Text(
                        text = foodItem.brand,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                Text(
                    text = "${foodItem.servingSize} ${foodItem.servingUnit}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = "${foodItem.calories} cal",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
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
