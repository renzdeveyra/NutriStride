package com.example.nutristride.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.nutristride.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(userProfile: UserProfile)
    
    @Update
    suspend fun updateUserProfile(userProfile: UserProfile)
    
    @Query("SELECT * FROM user_profiles WHERE userId = :userId")
    fun getUserProfile(userId: String): Flow<UserProfile?>
}
