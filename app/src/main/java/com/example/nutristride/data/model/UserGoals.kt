package com.example.nutristride.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId

@Entity(tableName = "user_goals")
data class UserGoals(
    @PrimaryKey
    @DocumentId
    val userId: String = "",
    val dailyCalorieTarget: Int = 2000,
    val dailyStepsTarget: Int = 10000,
    val dailyWaterTarget: Int = 2000, // in ml
    val weeklyWorkoutTarget: Int = 3,
    val proteinPercentage: Int = 30,
    val carbsPercentage: Int = 40,
    val fatPercentage: Int = 30,
    val currentWeight: Float? = null,
    val targetWeight: Float? = null,
    val weightGoalType: WeightGoalType = WeightGoalType.MAINTAIN
)

enum class WeightGoalType {
    LOSE,
    MAINTAIN,
    GAIN
}
