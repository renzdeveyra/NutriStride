package com.example.nutristride.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.nutristride.data.model.ActivityRecord
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.model.UserGoals
import com.example.nutristride.data.model.UserProfile

@Database(
    entities = [
        FoodItem::class,
        ActivityRecord::class,
        UserGoals::class,
        UserProfile::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NutriStrideDatabase : RoomDatabase() {
    
    abstract fun foodItemDao(): FoodItemDao
    abstract fun activityRecordDao(): ActivityRecordDao
    abstract fun userGoalsDao(): UserGoalsDao
    abstract fun userProfileDao(): UserProfileDao
    
    companion object {
        @Volatile
        private var INSTANCE: NutriStrideDatabase? = null
        
        fun getDatabase(context: Context): NutriStrideDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NutriStrideDatabase::class.java,
                    "nutristride_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
