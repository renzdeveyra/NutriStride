package com.example.nutristride.ui.screens.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.model.MealType
import com.example.nutristride.data.repository.FirestoreRepository
import com.example.nutristride.auth.FirebaseAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class FoodSearchViewModel @Inject constructor(
    private val firestoreRepository:  FirestoreRepository,
    private val authManager: FirebaseAuthManager
) : ViewModel() {
    
    private val _searchResults = MutableStateFlow<List<FoodItem>>(emptyList())
    val searchResults: StateFlow<List<FoodItem>> = _searchResults
    
    private val _recentFoods = MutableStateFlow<List<FoodItem>>(emptyList())
    val recentFoods: StateFlow<List<FoodItem>> = _recentFoods
    
    private val _frequentFoods = MutableStateFlow<List<FoodItem>>(emptyList())
    val frequentFoods: StateFlow<List<FoodItem>> = _frequentFoods
    
    private val _favoriteFoods = MutableStateFlow<List<FoodItem>>(emptyList())
    val favoriteFoods: StateFlow<List<FoodItem>> = _favoriteFoods
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    init {
        loadUserFoods()
    }
    
    private fun loadUserFoods() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = authManager.getCurrentUserId()
                if (userId != null) {
                    // Get all user's food items
                    val allFoods = firestoreRepository.getFoodItems(userId)
                    
                    // Recent foods (last 10 logged)
                    _recentFoods.value = allFoods
                        .sortedByDescending { it.dateAdded }
                        .take(10)
                    
                    // Frequent foods (most consumed)
                    _frequentFoods.value = allFoods
                        .sortedByDescending { it.consumptionCount }
                        .take(10)
                    
                    // Favorite foods
                    _favoriteFoods.value = allFoods
                        .filter { it.isFavorite }
                        .sortedBy { it.name }
                }
            } catch (e: Exception) {
                _error.value = "Failed to load food items: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun searchFood(query: String) {
        if (query.length < 3) {
            _searchResults.value = emptyList()
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                // Search in Firestore
                val userId = authManager.getCurrentUserId()
                if (userId != null) {
                    val results = firestoreRepository.searchFoodItems(query, userId)
                    _searchResults.value = results
                }
            } catch (e: Exception) {
                _error.value = "Search failed: ${e.message}"
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun toggleFavorite(foodItem: FoodItem) {
        viewModelScope.launch {
            try {
                val updatedItem = foodItem.copy(isFavorite = !foodItem.isFavorite)
                val success = firestoreRepository.saveFoodItem(updatedItem)
                
                if (success) {
                    // Update the lists that might contain this item
                    updateFoodItemInLists(updatedItem)
                }
            } catch (e: Exception) {
                _error.value = "Failed to update favorite status: ${e.message}"
            }
        }
    }
    
    private fun updateFoodItemInLists(updatedItem: FoodItem) {
        // Update in search results
        _searchResults.value = _searchResults.value.map { 
            if (it.id == updatedItem.id) updatedItem else it 
        }
        
        // Update in recent foods
        _recentFoods.value = _recentFoods.value.map { 
            if (it.id == updatedItem.id) updatedItem else it 
        }
        
        // Update in frequent foods
        _frequentFoods.value = _frequentFoods.value.map { 
            if (it.id == updatedItem.id) updatedItem else it 
        }
        
        // Update favorites list
        if (updatedItem.isFavorite) {
            if (_favoriteFoods.value.none { it.id == updatedItem.id }) {
                _favoriteFoods.value = _favoriteFoods.value + updatedItem
            }
        } else {
            _favoriteFoods.value = _favoriteFoods.value.filter { it.id != updatedItem.id }
        }
    }
    
    fun refreshData() {
        loadUserFoods()
    }
}