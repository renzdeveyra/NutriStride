package com.example.nutristride.data.repository

import com.example.nutristride.data.model.ActivityRecord
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.model.UserGoals
import com.example.nutristride.data.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepository @Inject constructor() {
    
    private val firestore = FirebaseFirestore.getInstance()
    
    // Collection references
    private val usersCollection = firestore.collection("users")
    private val foodItemsCollection = firestore.collection("food_items")
    private val activityRecordsCollection = firestore.collection("activity_records")
    
    // User Profile Operations
    suspend fun getUserProfile(userId: String): UserProfile? {
        return try {
            val document = usersCollection.document(userId).get().await()
            document.toObject(UserProfile::class.java)
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun saveUserProfile(userProfile: UserProfile): Boolean {
        return try {
            usersCollection.document(userProfile.userId)
                .set(userProfile, SetOptions.merge())
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    // User Goals Operations
    suspend fun getUserGoals(userId: String): UserGoals? {
        return try {
            val document = usersCollection.document(userId)
                .collection("goals").document("current_goals")
                .get().await()
            document.toObject(UserGoals::class.java)
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun saveUserGoals(userId: String, userGoals: UserGoals): Boolean {
        return try {
            usersCollection.document(userId)
                .collection("goals").document("current_goals")
                .set(userGoals, SetOptions.merge())
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
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
    
    suspend fun saveFoodItem(foodItem: FoodItem): Boolean {
        return try {
            val documentId = foodItem.id ?: foodItemsCollection.document().id
            val itemWithId = if (foodItem.id == null) foodItem.copy(id = documentId) else foodItem
            
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
            val documentId = activityRecord.id ?: activityRecordsCollection.document().id
            val recordWithId = if (activityRecord.id == null) activityRecord.copy(id = documentId) else activityRecord
            
            activityRecordsCollection.document(documentId)
                .set(recordWithId, SetOptions.merge())
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun deleteActivityRecord(activityRecordId: String): Boolean {
        return try {
            activityRecordsCollection.document(activityRecordId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    // Water Intake Operations
    suspend fun saveWaterIntake(userId: String, date: String, amount: Int): Boolean {
        return try {
            val waterIntakeData = hashMapOf(
                "amount" to amount,
                "date" to date
            )
            
            usersCollection.document(userId)
                .collection("water_intake").document(date)
                .set(waterIntakeData, SetOptions.merge())
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    suspend fun getWaterIntake(userId: String, date: String): Int {
        return try {
            val document = usersCollection.document(userId)
                .collection("water_intake").document(date)
                .get().await()
            
            document.getLong("amount")?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }
}
