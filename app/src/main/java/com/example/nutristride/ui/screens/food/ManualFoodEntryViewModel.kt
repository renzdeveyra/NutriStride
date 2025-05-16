package com.example.nutristride.ui.screens.food

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutristride.auth.FirebaseAuthManager
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.repository.FirestoreRepository
import com.example.nutristride.data.sync.DataSynchronizer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManualFoodEntryViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    val authManager: FirebaseAuthManager,  // Changed to public for direct access
    private val dataSynchronizer: DataSynchronizer
) : ViewModel() {
    
    private val TAG = "ManualFoodEntryVM"
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess
    
    fun saveFoodItem(foodItem: FoodItem) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = authManager.getCurrentUserId()
                if (userId != null) {
                    // Ensure the food item has the correct user ID
                    val itemWithUserId = if (foodItem.userId.isBlank()) {
                        foodItem.copy(userId = userId)
                    } else {
                        foodItem
                    }
                    
                    // Save to Firestore
                    val success = firestoreRepository.saveFoodItem(itemWithUserId)
                    if (success) {
                        _saveSuccess.value = true
                        
                        // Sync to local database
                        dataSynchronizer.syncFoodItemToCloud(itemWithUserId)
                        
                        Log.d(TAG, "Saved food item: ${itemWithUserId.name}")
                    } else {
                        _error.value = "Failed to save food item"
                    }
                } else {
                    _error.value = "User not logged in"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error saving food item: ${e.message}", e)
                _error.value = "Failed to save food item: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun resetState() {
        _saveSuccess.value = false
        _error.value = null
    }
}