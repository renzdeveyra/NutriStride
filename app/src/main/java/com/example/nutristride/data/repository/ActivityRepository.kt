package com.example.nutristride.data.repository

import com.example.nutristride.data.local.ActivityRecordDao
import com.example.nutristride.data.model.ActivityRecord
import com.example.nutristride.data.model.ActivityType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.Calendar
import java.util.Date
class ActivityRepository(
    private val activityRecordDao: ActivityRecordDao
) {
    suspend fun insertActivityRecord(activityRecord: ActivityRecord) {
        activityRecordDao.insertActivityRecord(activityRecord)
    }

    suspend fun addActivityRecord(activityRecord: ActivityRecord) {
        activityRecordDao.insertActivityRecord(activityRecord)
    }

    suspend fun updateActivityRecord(activityRecord: ActivityRecord) {
        activityRecordDao.updateActivityRecord(activityRecord)
    }

    suspend fun deleteActivityRecord(activityRecord: ActivityRecord) {
        activityRecordDao.deleteActivityRecord(activityRecord)
    }

    fun getAllActivityRecordsFlow(userId: String): Flow<List<ActivityRecord>> {
        return activityRecordDao.getAllActivityRecords(userId)
    }

    suspend fun getAllActivityRecords(userId: String): List<ActivityRecord> {
        return activityRecordDao.getAllActivityRecords(userId).firstOrNull() ?: emptyList()
    }

    suspend fun getActivityRecords(userId: String): List<ActivityRecord> {
        return activityRecordDao.getAllActivityRecords(userId).firstOrNull() ?: emptyList()
    }

    fun getActivityRecordsForToday(userId: String): Flow<List<ActivityRecord>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDate = calendar.time

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endDate = calendar.time

        return activityRecordDao.getActivityRecordsByDateRange(userId, startDate, endDate)
    }

    fun getActivityRecordsByType(userId: String, type: ActivityType): Flow<List<ActivityRecord>> {
        return activityRecordDao.getActivityRecordsByType(userId, type)
    }

    fun getTotalCaloriesBurnedForToday(userId: String): Flow<Int?> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDate = calendar.time

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endDate = calendar.time

        return activityRecordDao.getTotalCaloriesBurnedForDateRange(userId, startDate, endDate)
    }

    fun getTotalStepsForToday(userId: String): Flow<Int?> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDate = calendar.time

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endDate = calendar.time

        return activityRecordDao.getTotalStepsForDateRange(userId, startDate, endDate)
    }

    suspend fun getActivityRecordById(id: String): ActivityRecord? {
        return activityRecordDao.getActivityRecordById(id)
    }
}
