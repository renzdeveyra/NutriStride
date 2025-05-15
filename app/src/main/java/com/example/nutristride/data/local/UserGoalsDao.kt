package com.example.nutristride.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.nutristride.data.model.UserGoals
import kotlinx.coroutines.flow.Flow

@Dao
interface UserGoalsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserGoals(userGoals: UserGoals)
    
    @Update
    suspend fun updateUserGoals(userGoals: UserGoals)
    
    @Query("SELECT * FROM user_goals WHERE userId = :userId")
    fun getUserGoals(userId: String): Flow<UserGoals?>
}
