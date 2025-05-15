package com.example.nutristride.data.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.nutristride.auth.FirebaseAuthManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val dataSynchronizer: DataSynchronizer,
    private val authManager: FirebaseAuthManager
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        val userId = authManager.currentUserId ?: return Result.failure()
        
        return try {
            // Sync data to cloud
            dataSynchronizer.syncToCloud(userId)
            
            // Sync data from cloud
            dataSynchronizer.syncFromCloud(userId)
            
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
    
    companion object {
        const val WORK_NAME = "data_sync_worker"
    }
}
