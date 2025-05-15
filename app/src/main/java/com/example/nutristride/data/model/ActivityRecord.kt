package com.example.nutristride.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import java.util.Date
import java.util.UUID

@Entity(tableName = "activity_records")
data class ActivityRecord(
    @PrimaryKey
    @DocumentId
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val type: ActivityType = ActivityType.WALKING,
    val durationMinutes: Int = 0,
    val caloriesBurned: Int = 0,
    val date: Date = Date(),
    val distance: Float? = null,
    val steps: Int? = null,
    val userId: String? = null
)

enum class ActivityType {
    WALKING,
    RUNNING,
    CYCLING,
    SWIMMING,
    WEIGHT_TRAINING,
    YOGA,
    OTHER
}
