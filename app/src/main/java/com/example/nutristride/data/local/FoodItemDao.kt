package com.example.nutristride.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.model.MealType
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface FoodItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodItem(foodItem: FoodItem)
    
    @Update
    suspend fun updateFoodItem(foodItem: FoodItem)
    
    @Delete
    suspend fun deleteFoodItem(foodItem: FoodItem)
    
    @Query("SELECT * FROM food_items WHERE userId = :userId ORDER BY date DESC")
    fun getAllFoodItems(userId: String): Flow<List<FoodItem>>
    
    @Query("SELECT * FROM food_items WHERE userId = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getFoodItemsByDateRange(userId: String, startDate: Date, endDate: Date): Flow<List<FoodItem>>
    
    @Query("SELECT * FROM food_items WHERE userId = :userId AND mealType = :mealType AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getFoodItemsByMealType(userId: String, mealType: MealType, startDate: Date, endDate: Date): Flow<List<FoodItem>>
    
    @Query("SELECT * FROM food_items WHERE userId = :userId AND isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteFoodItems(userId: String): Flow<List<FoodItem>>
    
    @Query("SELECT SUM(calories) FROM food_items WHERE userId = :userId AND date BETWEEN :startDate AND :endDate")
    fun getTotalCaloriesForDateRange(userId: String, startDate: Date, endDate: Date): Flow<Int?>
    
    @Query("SELECT * FROM food_items WHERE id = :id")
    suspend fun getFoodItemById(id: String): FoodItem?
}
