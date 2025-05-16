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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.NoFood
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.model.MealType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale as JavaLocale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDiaryScreen(
    viewModel: FoodDiaryViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onAddFoodClick: () -> Unit,
    onFoodItemClick: (String) -> Unit
) {
    val foodItems by viewModel.foodItems.collectAsState()
    val totalCalories by viewModel.totalCalories.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    // Format date for display
    val dateFormatter = remember { SimpleDateFormat("EEEE, MMMM d, yyyy", JavaLocale.getDefault()) }
    val formattedDate = remember(selectedDate) { dateFormatter.format(selectedDate) }
    
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Date selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            val calendar = Calendar.getInstance()
                            calendar.time = selectedDate
                            calendar.add(Calendar.DAY_OF_MONTH, -1)
                            viewModel.setDate(calendar.time)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Previous Day"
                        )
                    }
                    
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    IconButton(
                        onClick = {
                            val calendar = Calendar.getInstance()
                            calendar.time = selectedDate
                            calendar.add(Calendar.DAY_OF_MONTH, 1)
                            viewModel.setDate(calendar.time)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Next Day"
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Total calories
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Total Calories",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "$totalCalories",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Food items by meal type
                if (foodItems.isEmpty() && !isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.NoFood,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No food logged for this day",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = onAddFoodClick
                            ) {
                                Text("Add Food")
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        // Sort meal types in chronological order
                        val sortedMealTypes = MealType.values().toList()
                        
                        sortedMealTypes.forEach { mealType ->
                            val mealItems = foodItems[mealType] ?: emptyList()
                            
                            if (mealItems.isNotEmpty()) {
                                item {
                                    Text(
                                        text = mealType.name.lowercase().capitalize(Locale.current),
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                                
                                items(mealItems) { foodItem ->
                                    FoodItemCard(
                                        foodItem = foodItem,
                                        onClick = { onFoodItemClick(foodItem.id) },
                                        onDelete = { viewModel.deleteFoodItem(foodItem) }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                                
                                item {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
            }
            
            // Loading indicator
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
            // Error handling
            error?.let { errorMessage ->
                LaunchedEffect(errorMessage) {
                    // Show error message (e.g., using a Snackbar)
                }
            }
        }
    }
}

@Composable
fun FoodItemCard(
    foodItem: FoodItem,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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