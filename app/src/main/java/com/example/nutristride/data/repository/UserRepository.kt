package com.example.nutristride.data.repository

import com.example.nutristride.data.local.UserGoalsDao
import com.example.nutristride.data.local.UserProfileDao
import com.example.nutristride.data.model.UserGoals
import com.example.nutristride.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class UserRepository(
    private val userProfileDao: UserProfileDao,
    private val userGoalsDao: UserGoalsDao
) {
    suspend fun insertUserProfile(userProfile: UserProfile) {
        userProfileDao.insertUserProfile(userProfile)
    }

    suspend fun saveUserProfile(userProfile: UserProfile) {
        userProfileDao.insertUserProfile(userProfile)
    }

    suspend fun updateUserProfile(userProfile: UserProfile) {
        userProfileDao.updateUserProfile(userProfile)
    }

    fun getUserProfileFlow(userId: String): Flow<UserProfile?> {
        return userProfileDao.getUserProfile(userId)
    }

    suspend fun getUserProfile(userId: String): UserProfile? {
        return userProfileDao.getUserProfile(userId).firstOrNull()
    }

    suspend fun insertUserGoals(userGoals: UserGoals) {
        userGoalsDao.insertUserGoals(userGoals)
    }

    suspend fun saveUserGoals(userGoals: UserGoals) {
        userGoalsDao.insertUserGoals(userGoals)
    }

    suspend fun updateUserGoals(userGoals: UserGoals) {
        userGoalsDao.updateUserGoals(userGoals)
    }

    fun getUserGoalsFlow(userId: String): Flow<UserGoals?> {
        return userGoalsDao.getUserGoals(userId)
    }

    suspend fun getUserGoals(userId: String): UserGoals? {
        return userGoalsDao.getUserGoals(userId).firstOrNull()
    }
}
