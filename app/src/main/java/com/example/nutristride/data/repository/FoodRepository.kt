package com.example.nutristride.data.repository

import com.example.nutristride.data.local.FoodItemDao
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.model.MealType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.Calendar
import java.util.Date
class FoodRepository(
    private val foodItemDao: FoodItemDao
) {
    suspend fun insertFoodItem(foodItem: FoodItem) {
        foodItemDao.insertFoodItem(foodItem)
    }

    suspend fun addFoodItem(foodItem: FoodItem) {
        foodItemDao.insertFoodItem(foodItem)
    }

    suspend fun updateFoodItem(foodItem: FoodItem) {
        foodItemDao.updateFoodItem(foodItem)
    }

    suspend fun deleteFoodItem(foodItem: FoodItem) {
        foodItemDao.deleteFoodItem(foodItem)
    }

    fun getAllFoodItemsFlow(userId: String): Flow<List<FoodItem>> {
        return foodItemDao.getAllFoodItems(userId)
    }

    suspend fun getAllFoodItems(userId: String): List<FoodItem> {
        return foodItemDao.getAllFoodItems(userId).firstOrNull() ?: emptyList()
    }

    suspend fun getFoodItems(userId: String): List<FoodItem> {
        return foodItemDao.getAllFoodItems(userId).firstOrNull() ?: emptyList()
    }

    fun getFoodItemsForToday(userId: String): Flow<List<FoodItem>> {
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

        return foodItemDao.getFoodItemsByDateRange(userId, startDate, endDate)
    }

    fun getFoodItemsByMealTypeForToday(userId: String, mealType: MealType): Flow<List<FoodItem>> {
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

        return foodItemDao.getFoodItemsByMealType(userId, mealType, startDate, endDate)
    }

    fun getFavoriteFoodItems(userId: String): Flow<List<FoodItem>> {
        return foodItemDao.getFavoriteFoodItems(userId)
    }

    fun getTotalCaloriesForToday(userId: String): Flow<Int?> {
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

        return foodItemDao.getTotalCaloriesForDateRange(userId, startDate, endDate)
    }

    suspend fun getFoodItemById(id: String): FoodItem? {
        return foodItemDao.getFoodItemById(id)
    }
}
