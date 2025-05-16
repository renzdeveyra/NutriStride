package com.example.nutristride.data.repository

import android.util.Log
import com.example.nutristride.data.api.OpenFoodFactsResponse
import com.example.nutristride.data.api.OpenFoodFactsSearchResponse
import com.example.nutristride.data.api.OpenFoodFactsService
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.model.MealType
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import java.util.Date

@Singleton
class FoodApiRepository @Inject constructor(
    private val openFoodFactsService: OpenFoodFactsService
) {
    private val TAG = "FoodApiRepository"

    suspend fun searchFoodByName(query: String): List<FoodItem> {
        return try {
            Log.d(TAG, "Searching for food with query: $query")
            val response = openFoodFactsService.searchProducts(query)
            Log.d(TAG, "Search response received: ${response.count} products found")
            mapProductsToFoodItems(response)
        } catch (e: Exception) {
            Log.e(TAG, "Error searching for food: ${e.message}", e)
            emptyList()
        }
    }

    suspend fun getFoodByBarcode(barcode: String): FoodItem? {
        return try {
            Log.d(TAG, "Getting food by barcode: $barcode")
            val response = openFoodFactsService.getProductByBarcode(barcode)
            Log.d(TAG, "Barcode response received: ${response.product.productName}")
            mapProductToFoodItem(response)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting food by barcode: ${e.message}", e)
            null
        }
    }

    private fun mapProductsToFoodItems(response: OpenFoodFactsSearchResponse): List<FoodItem> {
        return response.products.mapNotNull { product ->
            try {
                FoodItem(
                    id = UUID.randomUUID().toString(),
                    name = product.productName,
                    brand = product.brands,
                    calories = product.nutrients.calories?.toInt() ?: 0,
                    protein = product.nutrients.protein?.toDouble() ?: 0.0,
                    carbs = product.nutrients.carbs?.toDouble() ?: 0.0,
                    fat = product.nutrients.fat?.toDouble() ?: 0.0,
                    servingSize = parseServingSize(product.servingSize).toDouble(),
                    servingUnit = "g",
                    mealType = MealType.BREAKFAST,
                    date = Date()
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error mapping product to food item: ${e.message}", e)
                null
            }
        }
    }

    private fun mapProductToFoodItem(response: OpenFoodFactsResponse): FoodItem? {
        return try {
            val product = response.product
            FoodItem(
                id = UUID.randomUUID().toString(),
                name = product.productName,
                brand = product.brands,
                calories = product.nutrients.calories?.toInt() ?: 0,
                protein = product.nutrients.protein?.toDouble() ?: 0.0,
                carbs = product.nutrients.carbs?.toDouble() ?: 0.0,
                fat = product.nutrients.fat?.toDouble() ?: 0.0,
                servingSize = parseServingSize(product.servingSize).toDouble(),
                servingUnit = "g",
                mealType = MealType.BREAKFAST,
                date = Date()
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error mapping product to food item: ${e.message}", e)
            null
        }
    }

    private fun parseServingSize(servingSizeStr: String?): Float {
        if (servingSizeStr == null) return 100f
        
        val regex = "(\\d+(\\.\\d+)?)\\s*g".toRegex()
        val matchResult = regex.find(servingSizeStr)
        
        return matchResult?.groupValues?.get(1)?.toFloatOrNull() ?: 100f
    }
}