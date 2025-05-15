package com.example.nutristride.data.local

import androidx.room.TypeConverter
import com.example.nutristride.data.model.ActivityType
import com.example.nutristride.data.model.ActivityLevel
import com.example.nutristride.data.model.Gender
import com.example.nutristride.data.model.MealType
import com.example.nutristride.data.model.WeightGoalType
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
    
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
    
    @TypeConverter
    fun fromMealType(value: MealType): String {
        return value.name
    }
    
    @TypeConverter
    fun toMealType(value: String): MealType {
        return MealType.valueOf(value)
    }
    
    @TypeConverter
    fun fromActivityType(value: ActivityType): String {
        return value.name
    }
    
    @TypeConverter
    fun toActivityType(value: String): ActivityType {
        return ActivityType.valueOf(value)
    }
    
    @TypeConverter
    fun fromGender(value: Gender?): String? {
        return value?.name
    }
    
    @TypeConverter
    fun toGender(value: String?): Gender? {
        return value?.let { Gender.valueOf(it) }
    }
    
    @TypeConverter
    fun fromActivityLevel(value: ActivityLevel): String {
        return value.name
    }
    
    @TypeConverter
    fun toActivityLevel(value: String): ActivityLevel {
        return ActivityLevel.valueOf(value)
    }
    
    @TypeConverter
    fun fromWeightGoalType(value: WeightGoalType): String {
        return value.name
    }
    
    @TypeConverter
    fun toWeightGoalType(value: String): WeightGoalType {
        return WeightGoalType.valueOf(value)
    }
}
