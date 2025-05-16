package com.example.nutristride.ui.screens.food

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.model.MealType
import com.example.nutristride.data.repository.FirestoreRepository
import com.example.nutristride.auth.FirebaseAuthManager
import com.example.nutristride.data.sync.DataSynchronizer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class FoodDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val firestoreRepository:  FirestoreRepository,
    private val authManager: FirebaseAuthManager,
    private val dataSynchronizer: DataSynchronizer
) : ViewModel() {
    
    private val TAG = "FoodDetailsViewModel"
    
    private val _foodItem = MutableStateFlow<FoodItem?>(null)
    val foodItem: StateFlow<FoodItem?> = _foodItem
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess
    
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
                // Get food item from Firestore
                val item = firestoreRepository.getFoodItemById(foodId)
                if (item != null) {
                    _foodItem.value = item
                    _error.value = null
                } else {
                    _error.value = "Food item not found"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading food item: ${e.message}", e)
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
                
                // Update in Firestore
                val success = firestoreRepository.saveFoodItem(updatedItem)
                if (success) {
                    _foodItem.value = updatedItem
                    Log.d(TAG, "Toggled favorite status for ${updatedItem.name} to ${updatedItem.isFavorite}")
                    
                    // Sync to local database
                    dataSynchronizer.syncFoodItemToCloud(updatedItem)
                } else {
                    _error.value = "Failed to update favorite status"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error toggling favorite: ${e.message}", e)
                _error.value = "Failed to update favorite status: ${e.message}"
            }
        }
    }
    
    fun logFood(servingSize: Double, mealType: MealType) {
        val currentItem = _foodItem.value ?: return
        
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = authManager.getCurrentUserId() ?: throw Exception("User not logged in")
                
                // Create a new food item entry for the log
                val loggedFoodItem = currentItem.copy(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    servingSize = servingSize,
                    mealType = mealType,
                    date = Date()
                )
                
                // Save to Firestore
                val success = firestoreRepository.saveFoodItem(loggedFoodItem)
                if (success) {
                    _saveSuccess.value = true
                    
                    // Sync to local database
                    dataSynchronizer.syncFoodItemToCloud(loggedFoodItem)
                    
                    Log.d(TAG, "Logged food item: ${loggedFoodItem.name} for meal: $mealType")
                } else {
                    _error.value = "Failed to log food item"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error logging food: ${e.message}", e)
                _error.value = "Failed to log food: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}