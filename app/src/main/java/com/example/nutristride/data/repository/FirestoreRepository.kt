package com.example.nutristride.data.repository

import com.example.nutristride.data.model.ActivityRecord
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.model.MealType
import com.example.nutristride.data.model.UserGoals
import com.example.nutristride.data.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    // Collection references
    private val foodItemsCollection = firestore.collection("food_items")
    private val activityRecordsCollection = firestore.collection("activity_records")
    private val userGoalsCollection = firestore.collection("user_goals")
    private val userProfilesCollection = firestore.collection("user_profiles")
    
    // Food Items Operations
    suspend fun getFoodItems(userId: String): List<FoodItem> {
        return try {
            val querySnapshot = foodItemsCollection
                .whereEqualTo("userId", userId)
                .get().await()
            
            querySnapshot.documents.mapNotNull { document ->
                document.toObject(FoodItem::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun getFoodItemById(id: String): FoodItem? {
        return try {
            val document = foodItemsCollection.document(id).get().await()
            document.toObject(FoodItem::class.java)
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun saveFoodItem(foodItem: FoodItem): Boolean {
        return try {
            val documentId = foodItem.id.ifEmpty { foodItemsCollection.document().id }
            val itemWithId = if (foodItem.id.isEmpty()) foodItem.copy(id = documentId) else foodItem
            
            foodItemsCollection.document(documentId)
                .set(itemWithId, SetOptions.merge())
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun deleteFoodItem(foodItemId: String): Boolean {
        return try {
            foodItemsCollection.document(foodItemId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    // Search food items by query
    suspend fun searchFoodItems(query: String, userId: String): List<FoodItem> {
        return try {
            // First, search user's own food items
            val userItems = foodItemsCollection
                .whereEqualTo("userId", userId)
                .get().await()
                .documents
                .mapNotNull { it.toObject(FoodItem::class.java) }
                .filter { 
                    it.name.contains(query, ignoreCase = true) || 
                    (it.brand?.contains(query, ignoreCase = true) ?: false) 
                }
            
            // Then, search public food items
            val publicItems = foodItemsCollection
                .whereEqualTo("isPublic", true)
                .get().await()
                .documents
                .mapNotNull { it.toObject(FoodItem::class.java) }
                .filter { 
                    it.name.contains(query, ignoreCase = true) || 
                    (it.brand?.contains(query, ignoreCase = true) ?: false) 
                }
            
            // Combine and remove duplicates
            (userItems + publicItems).distinctBy { it.id }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Get user's favorite food items
    suspend fun getFavoriteFoodItems(userId: String): List<FoodItem> {
        return try {
            foodItemsCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("isFavorite", true)
                .get().await()
                .documents
                .mapNotNull { it.toObject(FoodItem::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Get user's recent food items
    suspend fun getRecentFoodItems(userId: String, limit: Int = 10): List<FoodItem> {
        return try {
            foodItemsCollection
                .whereEqualTo("userId", userId)
                .orderBy("dateAdded", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get().await()
                .documents
                .mapNotNull { it.toObject(FoodItem::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Get food items by meal type for a specific date
    suspend fun getFoodItemsByMealTypeAndDate(userId: String, mealType: MealType, date: Date): List<FoodItem> {
        // Create date range for the given day
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startDate = calendar.time
        
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endDate = calendar.time
        
        return try {
            foodItemsCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("mealType", mealType.toString())
                .whereGreaterThanOrEqualTo("date", startDate)
                .whereLessThanOrEqualTo("date", endDate)
                .get().await()
                .documents
                .mapNotNull { it.toObject(FoodItem::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    // Activity Records Operations
    suspend fun getActivityRecords(userId: String): List<ActivityRecord> {
        return try {
            val querySnapshot = activityRecordsCollection
                .whereEqualTo("userId", userId)
                .get().await()
            
            querySnapshot.documents.mapNotNull { document ->
                document.toObject(ActivityRecord::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    suspend fun saveActivityRecord(activityRecord: ActivityRecord): Boolean {
        return try {
            val documentId = activityRecord.id.ifEmpty { activityRecordsCollection.document().id }
            val recordWithId = if (activityRecord.id.isEmpty()) activityRecord.copy(id = documentId) else activityRecord
            
            activityRecordsCollection.document(documentId)
                .set(recordWithId, SetOptions.merge())
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    // User Goals Operations
    suspend fun getUserGoals(userId: String): UserGoals? {
        return try {
            val document = userGoalsCollection.document(userId).get().await()
            document.toObject(UserGoals::class.java)
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun saveUserGoals(userId: String, userGoals: UserGoals): Boolean {
        return try {
            userGoalsCollection.document(userId)
                .set(userGoals, SetOptions.merge())
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    // User Profile Operations
    suspend fun getUserProfile(userId: String): UserProfile? {
        return try {
            val document = userProfilesCollection.document(userId).get().await()
            document.toObject(UserProfile::class.java)
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun saveUserProfile(userProfile: UserProfile): Boolean {
        return try {
            userProfilesCollection.document(userProfile.userId)
                .set(userProfile, SetOptions.merge())
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
