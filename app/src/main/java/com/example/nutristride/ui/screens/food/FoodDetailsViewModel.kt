package com.example.nutristride.ui.screens.food

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.model.MealType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class FoodDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val TAG = "FoodDetailsViewModel"
    
    private val _foodItem = MutableStateFlow<FoodItem?>(null)
    val foodItem: StateFlow<FoodItem?> = _foodItem
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    init {
        val foodId = savedStateHandle.get<String>("foodId")
        if (foodId != null) {
            loadFoodItem(foodId)
        } else {
            _error.value = "Food ID not found"
        }
    }
    
    private fun loadFoodItem(foodId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // TODO: Replace with actual repository call
                // For now, create a dummy food item
                _foodItem.value = FoodItem(
                    id = foodId,
                    name = "Sample Food",
                    brand = "Sample Brand",
                    calories = 250,
                    protein = 10f,
                    carbs = 30f,
                    fat = 8f,
                    servingSize = 100f,
                    servingUnit = "g",
                    isFavorite = false,
                    date = Date(),
                    mealType = MealType.BREAKFAST
                )
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun toggleFavorite() {
        val currentItem = _foodItem.value ?: return
        
        viewModelScope.launch {
            try {
                val updatedItem = currentItem.copy(isFavorite = !currentItem.isFavorite)
                // TODO: Update in repository
                _foodItem.value = updatedItem
                Log.d(TAG, "Toggled favorite status for ${updatedItem.name} to ${updatedItem.isFavorite}")
            } catch (e: Exception) {
                Log.e(TAG, "Error toggling favorite: ${e.message}", e)
                _error.value = "Failed to update favorite status: ${e.message}"
            }
        }
    }
}