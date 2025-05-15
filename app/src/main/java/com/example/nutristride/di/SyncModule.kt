package com.example.nutristride.di

import android.content.Context
import com.example.nutristride.data.repository.ActivityRepository
import com.example.nutristride.data.repository.FirestoreRepository
import com.example.nutristride.data.repository.FoodRepository
import com.example.nutristride.data.repository.UserRepository
import com.example.nutristride.data.sync.DataSynchronizer
import com.example.nutristride.data.sync.SyncManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SyncModule {

    @Provides
    @Singleton
    fun provideDataSynchronizer(
        firestoreRepository: FirestoreRepository,
        userRepository: UserRepository,
        foodRepository: FoodRepository,
        activityRepository: ActivityRepository
    ): DataSynchronizer {
        return DataSynchronizer(
            firestoreRepository,
            userRepository,
            foodRepository,
            activityRepository
        )
    }

    @Provides
    @Singleton
    fun provideSyncManager(
        @ApplicationContext context: Context
    ): SyncManager {
        return SyncManager(context)
    }
}
