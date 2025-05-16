package com.example.nutristride.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import java.util.Date

@Entity(tableName = "food_items")
data class FoodItem(
    @PrimaryKey
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val brand: String? = null,
    val calories: Int = 0,
    val protein: Float = 0f,
    val carbs: Float = 0f,
    val fat: Float = 0f,
    val servingSize: Float = 0f,
    val servingUnit: String = "g",
    val isFavorite: Boolean = false,
    val dateAdded: Long = System.currentTimeMillis(),
    val lastConsumed: Long? = null,
    val consumptionCount: Int = 0,
    val date: Date = Date(),
    val mealType: MealType = MealType.BREAKFAST
)
