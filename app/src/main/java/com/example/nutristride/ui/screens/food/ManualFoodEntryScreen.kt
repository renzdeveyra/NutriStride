package com.example.nutristride.ui.screens.food

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
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.model.MealType
import java.util.UUID
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualFoodEntryScreen(
    viewModel: ManualFoodEntryViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onSaveFood: (FoodItem) -> Unit
) {
    var foodName by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var servingSize by remember { mutableStateOf("") }
    var servingUnit by remember { mutableStateOf("g") }
    var selectedMealType by remember { mutableStateOf(MealType.BREAKFAST) }
    var saveToMyFoods by remember { mutableStateOf(false) }
    
    var servingUnitExpanded by remember { mutableStateOf(false) }
    val servingUnitOptions = listOf("g", "ml", "oz", "cup", "tbsp", "tsp", "piece")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Food Manually") },
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
            // Food Name
            OutlinedTextField(
                value = foodName,
                onValueChange = { foodName = it },
                label = { Text("Food Name*") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Brand (Optional)
            OutlinedTextField(
                value = brand,
                onValueChange = { brand = it },
                label = { Text("Brand (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Calories
            OutlinedTextField(
                value = calories,
                onValueChange = { calories = it },
                label = { Text("Calories*") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Macronutrients
            OutlinedTextField(
                value = protein,
                onValueChange = { protein = it },
                label = { Text("Protein (g)*") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = carbs,
                onValueChange = { carbs = it },
                label = { Text("Carbohydrates (g)*") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = fat,
                onValueChange = { fat = it },
                label = { Text("Fat (g)*") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Serving Size
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = servingSize,
                    onValueChange = { servingSize = it },
                    label = { Text("Serving Size*") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.padding(8.dp))
                
                ExposedDropdownMenuBox(
                    expanded = servingUnitExpanded,
                    onExpandedChange = { servingUnitExpanded = it },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = servingUnit,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Unit") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = servingUnitExpanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = servingUnitExpanded,
                        onDismissRequest = { servingUnitExpanded = false }
                    ) {
                        servingUnitOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    servingUnit = option
                                    servingUnitExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Meal Type Selection
            Text(
                text = "Add to Meal",
                style = MaterialTheme.typography.titleMedium
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
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Save to My Foods Checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = saveToMyFoods,
                    onCheckedChange = { saveToMyFoods = it }
                )
                Text("Save to My Foods for future use")
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Save Button
            Button(
                onClick = {
                    // Validate inputs
                    if (foodName.isNotBlank() &&
                        calories.isNotBlank() &&
                        protein.isNotBlank() &&
                        carbs.isNotBlank() &&
                        fat.isNotBlank() &&
                        servingSize.isNotBlank()
                    ) {
                        // Use the authManager from viewModel instead of calling getCurrentUserId directly
                        val userId = viewModel.authManager.currentUserId ?: ""
                        
                        val newFoodItem = FoodItem(
                            id = UUID.randomUUID().toString(),
                            userId = userId,
                            name = foodName,
                            brand = if (brand.isBlank()) null else brand,
                            calories = calories.toIntOrNull() ?: 0,
                            protein = protein.toDoubleOrNull() ?: 0.0,
                            carbs = carbs.toDoubleOrNull() ?: 0.0,
                            fat = fat.toDoubleOrNull() ?: 0.0,
                            servingSize = servingSize.toDoubleOrNull() ?: 0.0,
                            servingUnit = servingUnit,
                            mealType = selectedMealType,
                            isFavorite = saveToMyFoods,
                            dateAdded = Date(),
                            date = Date(),
                            isPublic = false,
                            consumptionCount = 1
                        )
                        onSaveFood(newFoodItem)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = foodName.isNotBlank() &&
                        calories.isNotBlank() &&
                        protein.isNotBlank() &&
                        carbs.isNotBlank() &&
                        fat.isNotBlank() &&
                        servingSize.isNotBlank()
            ) {
                Text("Save & Log Food")
            }
        }
    }
}
