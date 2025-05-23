package com.example.nutristride.ui.screens.food

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutristride.auth.FirebaseAuthManager
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.model.MealType
import com.example.nutristride.data.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class FoodLogViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val authManager: FirebaseAuthManager
) : ViewModel() {
    
    private val TAG = "FoodLogViewModel"
    
    private val _foodItems = MutableStateFlow<Map<MealType, List<FoodItem>>>(emptyMap())
    val foodItems: StateFlow<Map<MealType, List<FoodItem>>> = _foodItems
    
    private val _totalCalories = MutableStateFlow(0)
    val totalCalories: StateFlow<Int> = _totalCalories
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    init {
        loadTodaysFoodItems()
    }
    
    private fun loadTodaysFoodItems() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = authManager.getCurrentUserId()
                if (userId != null) {
                    // Get today's date range
                    val calendar = Calendar.getInstance()
                    val today = calendar.time
                    
                    val allMealTypes = MealType.values()
                    val foodItemsByMealType = mutableMapOf<MealType, List<FoodItem>>()
                    var totalCals = 0
                    
                    // Load food items for each meal type
                    for (mealType in allMealTypes) {
                        val items = firestoreRepository.getFoodItemsByMealTypeAndDate(userId, mealType, today)
                        if (items.isNotEmpty()) {
                            foodItemsByMealType[mealType] = items
                            totalCals += items.sumOf { it.calories }
                        }
                    }
                    
                    _foodItems.value = foodItemsByMealType
                    _totalCalories.value = totalCals
                    
                    Log.d(TAG, "Loaded ${foodItemsByMealType.values.flatten().size} food items for today")
                } else {
                    Log.e(TAG, "User not logged in")
                    _error.value = "User not logged in"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading food items: ${e.message}", e)
                _error.value = "Failed to load food items: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun deleteFoodItem(foodItem: FoodItem) {
        viewModelScope.launch {
            try {
                val success = firestoreRepository.deleteFoodItem(foodItem.id)
                if (success) {
                    // Refresh food items after deletion
                    loadTodaysFoodItems()
                    Log.d(TAG, "Deleted food item: ${foodItem.name}")
                } else {
                    _error.value = "Failed to delete food item"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting food item: ${e.message}", e)
                _error.value = "Failed to delete food item: ${e.message}"
            }
        }
    }
    
    fun refreshData() {
        loadTodaysFoodItems()
    }
}