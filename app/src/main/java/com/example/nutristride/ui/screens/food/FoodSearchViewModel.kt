package com.example.nutristride.ui.screens.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.repository.FoodApiRepository
import com.example.nutristride.data.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodSearchViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val foodApiRepository: FoodApiRepository
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<FoodItem>>(emptyList())
    val searchResults: StateFlow<List<FoodItem>> = _searchResults

    private val _recentFoods = MutableStateFlow<List<FoodItem>>(emptyList())
    val recentFoods: StateFlow<List<FoodItem>> = _recentFoods

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadRecentFoods()
    }

    fun searchFood(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                val results = foodApiRepository.searchFoodByName(query)
                _searchResults.value = results
            } catch (e: Exception) {
                _error.value = "Failed to search for food: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun scanBarcode(barcode: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                val foodItem = foodApiRepository.getFoodByBarcode(barcode)
                _searchResults.value = listOfNotNull(foodItem)
            } catch (e: Exception) {
                _error.value = "Failed to get food by barcode: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveFoodItem(foodItem: FoodItem, userId: String?) {
        viewModelScope.launch {
            try {
                val itemWithUserId = foodItem.copy(userId = userId)
                foodRepository.insertFoodItem(itemWithUserId)
            } catch (e: Exception) {
                _error.value = "Failed to save food item: ${e.message}"
            }
        }
    }

    private fun loadRecentFoods() {
        viewModelScope.launch {
            try {
                // This would need to be implemented in the FoodRepository
                // to get recent food items for the current user
                // _recentFoods.value = foodRepository.getRecentFoodItems(userId)
            } catch (e: Exception) {
                _error.value = "Failed to load recent foods: ${e.message}"
            }
        }
    }
}