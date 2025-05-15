package com.example.nutristride.di

import android.content.Context
import com.example.nutristride.data.local.ActivityRecordDao
import com.example.nutristride.data.local.FoodItemDao
import com.example.nutristride.data.local.NutriStrideDatabase
import com.example.nutristride.data.local.UserGoalsDao
import com.example.nutristride.data.local.UserProfileDao
import com.example.nutristride.data.repository.ActivityRepository
import com.example.nutristride.data.repository.FoodRepository
import com.example.nutristride.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideNutriStrideDatabase(@ApplicationContext context: Context): NutriStrideDatabase {
        return NutriStrideDatabase.getDatabase(context)
    }
    
    @Provides
    @Singleton
    fun provideFoodItemDao(database: NutriStrideDatabase): FoodItemDao {
        return database.foodItemDao()
    }
    
    @Provides
    @Singleton
    fun provideActivityRecordDao(database: NutriStrideDatabase): ActivityRecordDao {
        return database.activityRecordDao()
    }
    
    @Provides
    @Singleton
    fun provideUserGoalsDao(database: NutriStrideDatabase): UserGoalsDao {
        return database.userGoalsDao()
    }
    
    @Provides
    @Singleton
    fun provideUserProfileDao(database: NutriStrideDatabase): UserProfileDao {
        return database.userProfileDao()
    }
    
    @Provides
    @Singleton
    fun provideFoodRepository(foodItemDao: FoodItemDao): FoodRepository {
        return FoodRepository(foodItemDao)
    }
    
    @Provides
    @Singleton
    fun provideActivityRepository(activityRecordDao: ActivityRecordDao): ActivityRepository {
        return ActivityRepository(activityRecordDao)
    }
    
    @Provides
    @Singleton
    fun provideUserRepository(
        userProfileDao: UserProfileDao,
        userGoalsDao: UserGoalsDao
    ): UserRepository {
        return UserRepository(userProfileDao, userGoalsDao)
    }
}
