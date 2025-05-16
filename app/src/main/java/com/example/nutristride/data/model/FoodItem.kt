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
    val protein: Double = 0.0,
    val carbs: Double = 0.0,
    val fat: Double = 0.0,
    val servingSize: Double = 0.0,
    val servingUnit: String = "g",
    val isFavorite: Boolean = false,
    val dateAdded: Date = Date(),
    val consumptionCount: Int = 0,
    val mealType: MealType = MealType.BREAKFAST,
    val date: Date? = null,
    val isPublic: Boolean = false
)
