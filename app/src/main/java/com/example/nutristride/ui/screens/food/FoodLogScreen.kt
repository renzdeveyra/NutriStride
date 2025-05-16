package com.example.nutristride.ui.screens.food

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.model.MealType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodLogScreen(
    viewModel: FoodLogViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onScanBarcodeClick: () -> Unit,
    onFoodItemClick: (String) -> Unit = {}
) {
    val foodItems by viewModel.foodItems.collectAsState()
    val totalCalories by viewModel.totalCalories.collectAsState()
    val date = Date() // Could be passed as parameter or from viewModel
    val dateFormat = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(date)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Food Log") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Add search and barcode scan buttons
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Foods"
                        )
                    }
                    IconButton(onClick = onScanBarcodeClick) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = "Scan Barcode"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onSearchClick,
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
            // Date and calories summary
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Total Calories: $totalCalories kcal",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Food items by meal type
            if (foodItems.isEmpty()) {
                EmptyFoodLog(onAddClick = onSearchClick)
            } else {
                FoodLogContent(
                    foodItems = foodItems,
                    onFoodItemClick = onFoodItemClick,
                    onDeleteFoodItem = { viewModel.deleteFoodItem(it) }
                )
            }
        }
    }
}

@Composable
fun EmptyFoodLog(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No food logged today",
            style = MaterialTheme.typography.bodyLarge
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Tap the + button to add food from the Open Food Facts database",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
fun FoodLogContent(
    foodItems: Map<MealType, List<FoodItem>>,
    onFoodItemClick: (String) -> Unit,
    onDeleteFoodItem: (FoodItem) -> Unit
) {
    LazyColumn {
        // Breakfast
        item {
            FoodLogMealHeader(
                mealType = MealType.BREAKFAST,
                totalCalories = foodItems[MealType.BREAKFAST]?.sumOf { it.calories } ?: 0
            )
        }
        
        items(foodItems[MealType.BREAKFAST] ?: emptyList()) { foodItem ->
            FoodLogItemRow(
                foodItem = foodItem,
                onClick = { onFoodItemClick(foodItem.id) },
                onDelete = { onDeleteFoodItem(foodItem) }
            )
        }
        
        item { Spacer(modifier = Modifier.height(16.dp)) }
        
        // Lunch
        item {
            FoodLogMealHeader(
                mealType = MealType.LUNCH,
                totalCalories = foodItems[MealType.LUNCH]?.sumOf { it.calories } ?: 0
            )
        }
        
        items(foodItems[MealType.LUNCH] ?: emptyList()) { foodItem ->
            FoodLogItemRow(
                foodItem = foodItem,
                onClick = { onFoodItemClick(foodItem.id) },
                onDelete = { onDeleteFoodItem(foodItem) }
            )
        }
        
        item { Spacer(modifier = Modifier.height(16.dp)) }
        
        // Dinner
        item {
            FoodLogMealHeader(
                mealType = MealType.DINNER,
                totalCalories = foodItems[MealType.DINNER]?.sumOf { it.calories } ?: 0
            )
        }
        
        items(foodItems[MealType.DINNER] ?: emptyList()) { foodItem ->
            FoodLogItemRow(
                foodItem = foodItem,
                onClick = { onFoodItemClick(foodItem.id) },
                onDelete = { onDeleteFoodItem(foodItem) }
            )
        }
        
        item { Spacer(modifier = Modifier.height(16.dp)) }
        
        // Snack
        item {
            FoodLogMealHeader(
                mealType = MealType.SNACK,
                totalCalories = foodItems[MealType.SNACK]?.sumOf { it.calories } ?: 0
            )
        }
        
        items(foodItems[MealType.SNACK] ?: emptyList()) { foodItem ->
            FoodLogItemRow(
                foodItem = foodItem,
                onClick = { onFoodItemClick(foodItem.id) },
                onDelete = { onDeleteFoodItem(foodItem) }
            )
        }
    }
}

@Composable
fun FoodLogMealHeader(mealType: MealType, totalCalories: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = when (mealType) {
                MealType.BREAKFAST -> "Breakfast"
                MealType.LUNCH -> "Lunch"
                MealType.DINNER -> "Dinner"
                MealType.SNACK -> "Snack"
            },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = "$totalCalories kcal",
            style = MaterialTheme.typography.bodyMedium
        )
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
    }
}

@Composable
fun FoodLogItemRow(
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
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
                    text = "P: ${foodItem.protein}g • C: ${foodItem.carbs}g • F: ${foodItem.fat}g",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            Text(
                text = "${foodItem.calories} kcal",
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