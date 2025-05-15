package com.example.nutristride.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import java.util.Date

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey
    @DocumentId
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val age: Int? = null,
    val gender: Gender? = null,
    val height: Float? = null, // in cm
    val activityLevel: ActivityLevel = ActivityLevel.MODERATE,
    val joinDate: Date = Date()
)

enum class Gender {
    MALE,
    FEMALE,
    OTHER
}

enum class ActivityLevel {
    SEDENTARY,
    LIGHT,
    MODERATE,
    VERY_ACTIVE,
    EXTREMELY_ACTIVE
}
