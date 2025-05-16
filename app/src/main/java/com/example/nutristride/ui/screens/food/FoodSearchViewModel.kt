package com.example.nutristride.ui.screens.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.model.MealType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class FoodSearchViewModel @Inject constructor() : ViewModel() {
    
    private val _searchResults = MutableStateFlow<List<FoodItem>>(emptyList())
    val searchResults: StateFlow<List<FoodItem>> = _searchResults
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    fun searchFood(query: String) {
        if (query.length < 3) return
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                // Simulate network delay
                delay(1000)
                
                // TODO: Replace with actual API call
                // For now, create dummy search results
                val results = listOf(
                    FoodItem(
                        id = "1",
                        name = "Apple",
                        brand = null,
                        calories = 95,
                        protein = 0.5f,
                        carbs = 25f,
                        fat = 0.3f,
                        servingSize = 100f,
                        servingUnit = "g",
                        isFavorite = false,
                        date = Date(),
                        mealType = MealType.BREAKFAST
                    ),
                    FoodItem(
                        id = "2",
                        name = "Banana",
                        brand = null,
                        calories = 105,
                        protein = 1.3f,
                        carbs = 27f,
                        fat = 0.4f,
                        servingSize = 100f,
                        servingUnit = "g",
                        isFavorite = true,
                        date = Date(),
                        mealType = MealType.BREAKFAST
                    ),
                    FoodItem(
                        id = "3",
                        name = "Chicken Breast",
                        brand = "Organic Farms",
                        calories = 165,
                        protein = 31f,
                        carbs = 0f,
                        fat = 3.6f,
                        servingSize = 100f,
                        servingUnit = "g",
                        isFavorite = false,
                        date = Date(),
                        mealType = MealType.LUNCH
                    )
                ).filter { 
                    it.name.contains(query, ignoreCase = true) || 
                    (it.brand?.contains(query, ignoreCase = true) ?: false) 
                }
                
                _searchResults.value = results
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}