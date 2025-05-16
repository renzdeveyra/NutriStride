package com.example.nutristride.ui.screens.food

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
class FoodDiaryViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val authManager: FirebaseAuthManager
) : ViewModel() {
    
    private val _foodItems = MutableStateFlow<Map<MealType, List<FoodItem>>>(emptyMap())
    val foodItems: StateFlow<Map<MealType, List<FoodItem>>> = _foodItems
    
    private val _selectedDate = MutableStateFlow(Calendar.getInstance().time)
    val selectedDate: StateFlow<Date> = _selectedDate
    
    private val _totalCalories = MutableStateFlow(0)
    val totalCalories: StateFlow<Int> = _totalCalories
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    init {
        loadFoodItemsForDate(_selectedDate.value)
    }
    
    fun setDate(date: Date) {
        _selectedDate.value = date
        loadFoodItemsForDate(date)
    }
    
    private fun loadFoodItemsForDate(date: Date) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = authManager.getCurrentUserId()
                if (userId != null) {
                    val allMealTypes = MealType.values()
                    val foodItemsByMealType = mutableMapOf<MealType, List<FoodItem>>()
                    var totalCals = 0
                    
                    // Load food items for each meal type
                    for (mealType in allMealTypes) {
                        val items = firestoreRepository.getFoodItemsByMealTypeAndDate(userId, mealType, date)
                        if (items.isNotEmpty()) {
                            foodItemsByMealType[mealType] = items
                            totalCals += items.sumOf { it.calories }
                        }
                    }
                    
                    _foodItems.value = foodItemsByMealType
                    _totalCalories.value = totalCals
                }
            } catch (e: Exception) {
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
                    loadFoodItemsForDate(_selectedDate.value)
                } else {
                    _error.value = "Failed to delete food item"
                }
            } catch (e: Exception) {
                _error.value = "Failed to delete food item: ${e.message}"
            }
        }
    }
    
    fun refreshData() {
        loadFoodItemsForDate(_selectedDate.value)
    }
}