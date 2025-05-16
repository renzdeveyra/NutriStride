package com.example.nutristride.data.sync

import com.example.nutristride.data.model.ActivityRecord
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.model.UserGoals
import com.example.nutristride.data.model.UserProfile
import com.example.nutristride.data.repository.ActivityRepository
import com.example.nutristride.data.repository.FirestoreRepository
import com.example.nutristride.data.repository.FoodRepository
import com.example.nutristride.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

@Singleton
class DataSynchronizer @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val userRepository: UserRepository,
    private val foodRepository: FoodRepository,
    private val activityRepository: ActivityRepository
) {
    
    /**
     * Synchronizes all user data from local database to Firestore
     */
    suspend fun syncToCloud(userId: String) {
        withContext(Dispatchers.IO) {
            // Sync user profile
            val userProfile = userRepository.getUserProfile(userId)
            userProfile?.let {
                firestoreRepository.saveUserProfile(it)
            }
            
            // Sync user goals
            val userGoals = userRepository.getUserGoals(userId)
            userGoals?.let {
                firestoreRepository.saveUserGoals(userId, it)
            }
            
            // Sync food items
            val foodItems = foodRepository.getFoodItems(userId)
            foodItems.forEach { foodItem ->
                firestoreRepository.saveFoodItem(foodItem)
            }
            
            // Sync activity records
            val activityRecords = activityRepository.getActivityRecords(userId)
            activityRecords.forEach { activityRecord ->
                firestoreRepository.saveActivityRecord(activityRecord)
            }
        }
    }
    
    /**
     * Synchronizes all user data from Firestore to local database
     */
    suspend fun syncFromCloud(userId: String) {
        withContext(Dispatchers.IO) {
            // Sync user profile
            val userProfile = firestoreRepository.getUserProfile(userId)
            userProfile?.let {
                userRepository.insertUserProfile(it)
            }
            
            // Sync user goals
            val userGoals = firestoreRepository.getUserGoals(userId)
            userGoals?.let {
                userRepository.insertUserGoals(it)
            }
            
            // Sync food items
            val foodItems = firestoreRepository.getFoodItems(userId)
            foodItems.forEach { foodItem ->
                foodRepository.insertFoodItem(foodItem)
            }
            
            // Sync activity records
            val activityRecords = firestoreRepository.getActivityRecords(userId)
            activityRecords.forEach { activityRecord ->
                activityRepository.insertActivityRecord(activityRecord)
            }
        }
    }
    
    /**
     * Synchronizes a specific user profile to Firestore
     */
    suspend fun syncUserProfileToCloud(userProfile: UserProfile) {
        withContext(Dispatchers.IO) {
            firestoreRepository.saveUserProfile(userProfile)
        }
    }
    
    /**
     * Synchronizes a specific user goals to Firestore
     */
    suspend fun syncUserGoalsToCloud(userId: String, userGoals: UserGoals) {
        withContext(Dispatchers.IO) {
            firestoreRepository.saveUserGoals(userId, userGoals)
        }
    }
    
    /**
     * Synchronizes a single food item to Firestore
     */
    suspend fun syncFoodItemToCloud(foodItem: FoodItem) {
        withContext(Dispatchers.IO) {
            try {
                firestoreRepository.saveFoodItem(foodItem)
            } catch (e: Exception) {
                Log.e("DataSynchronizer", "Error syncing food item to cloud: ${e.message}", e)
            }
        }
    }
    
    /**
     * Synchronizes a single food item from Firestore to local database
     */
    suspend fun syncFoodItemFromCloud(foodItemId: String) {
        withContext(Dispatchers.IO) {
            try {
                val foodItem = firestoreRepository.getFoodItemById(foodItemId)
                foodItem?.let {
                    foodRepository.insertFoodItem(it)
                }
            } catch (e: Exception) {
                Log.e("DataSynchronizer", "Error syncing food item from cloud: ${e.message}", e)
            }
        }
    }
    
    /**
     * Synchronizes a specific activity record to Firestore
     */
    suspend fun syncActivityRecordToCloud(activityRecord: ActivityRecord) {
        withContext(Dispatchers.IO) {
            firestoreRepository.saveActivityRecord(activityRecord)
        }
    }
    
    /**
     * Performs a background sync of all data
     */
    fun performBackgroundSync(userId: String, scope: CoroutineScope) {
        scope.launch {
            syncToCloud(userId)
            syncFromCloud(userId)
        }
    }
}
