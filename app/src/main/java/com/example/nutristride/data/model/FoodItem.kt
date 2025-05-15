package com.example.nutristride.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import java.util.Date
import java.util.UUID

@Entity(tableName = "food_items")
data class FoodItem(
    @PrimaryKey
    @DocumentId
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val brand: String? = null,
    val calories: Int = 0,
    val protein: Float = 0f,
    val carbs: Float = 0f,
    val fat: Float = 0f,
    val servingSize: Float = 0f,
    val servingUnit: String = "g",
    val mealType: MealType = MealType.BREAKFAST,
    val date: Date = Date(),
    val isFavorite: Boolean = false,
    val userId: String? = null
)

enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK
}
