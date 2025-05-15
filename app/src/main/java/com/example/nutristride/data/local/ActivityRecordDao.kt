package com.example.nutristride.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.nutristride.data.model.ActivityRecord
import com.example.nutristride.data.model.ActivityType
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface ActivityRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityRecord(activityRecord: ActivityRecord)
    
    @Update
    suspend fun updateActivityRecord(activityRecord: ActivityRecord)
    
    @Delete
    suspend fun deleteActivityRecord(activityRecord: ActivityRecord)
    
    @Query("SELECT * FROM activity_records WHERE userId = :userId ORDER BY date DESC")
    fun getAllActivityRecords(userId: String): Flow<List<ActivityRecord>>
    
    @Query("SELECT * FROM activity_records WHERE userId = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getActivityRecordsByDateRange(userId: String, startDate: Date, endDate: Date): Flow<List<ActivityRecord>>
    
    @Query("SELECT * FROM activity_records WHERE userId = :userId AND type = :type ORDER BY date DESC")
    fun getActivityRecordsByType(userId: String, type: ActivityType): Flow<List<ActivityRecord>>
    
    @Query("SELECT SUM(caloriesBurned) FROM activity_records WHERE userId = :userId AND date BETWEEN :startDate AND :endDate")
    fun getTotalCaloriesBurnedForDateRange(userId: String, startDate: Date, endDate: Date): Flow<Int?>
    
    @Query("SELECT SUM(steps) FROM activity_records WHERE userId = :userId AND date BETWEEN :startDate AND :endDate")
    fun getTotalStepsForDateRange(userId: String, startDate: Date, endDate: Date): Flow<Int?>
    
    @Query("SELECT * FROM activity_records WHERE id = :id")
    suspend fun getActivityRecordById(id: String): ActivityRecord?
}
