package com.example.nutristride.ui.screens.food

import androidx.lifecycle.ViewModel
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.model.MealType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FoodLogViewModel @Inject constructor() : ViewModel() {
    
    private val _foodItems = MutableStateFlow<Map<MealType, List<FoodItem>>>(emptyMap())
    val foodItems: StateFlow<Map<MealType, List<FoodItem>>> = _foodItems
    
    private val _totalCalories = MutableStateFlow(0)
    val totalCalories: StateFlow<Int> = _totalCalories
    
    fun deleteFoodItem(foodItem: FoodItem) {
        // Implementation will be added later
    }
}