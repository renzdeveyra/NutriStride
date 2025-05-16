package com.example.nutristride.data.repository

import com.example.nutristride.data.api.OpenFoodFactsResponse
import com.example.nutristride.data.api.OpenFoodFactsSearchResponse
import com.example.nutristride.data.api.OpenFoodFactsService
import com.example.nutristride.data.model.FoodItem
import com.example.nutristride.data.model.MealType
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodApiRepository @Inject constructor(
    private val openFoodFactsService: OpenFoodFactsService
) {
    suspend fun searchFoodByName(query: String): List<FoodItem> {
        return try {
            val response = openFoodFactsService.searchProducts(query)
            mapProductsToFoodItems(response)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getFoodByBarcode(barcode: String): FoodItem? {
        return try {
            val response = openFoodFactsService.getProductByBarcode(barcode)
            mapProductToFoodItem(response)
        } catch (e: Exception) {
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
                    protein = product.nutrients.protein ?: 0f,
                    carbs = product.nutrients.carbs ?: 0f,
                    fat = product.nutrients.fat ?: 0f,
                    servingSize = parseServingSize(product.servingSize),
                    servingUnit = "g",
                    mealType = MealType.BREAKFAST
                )
            } catch (e: Exception) {
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
                protein = product.nutrients.protein ?: 0f,
                carbs = product.nutrients.carbs ?: 0f,
                fat = product.nutrients.fat ?: 0f,
                servingSize = parseServingSize(product.servingSize),
                servingUnit = "g",
                mealType = MealType.BREAKFAST
            )
        } catch (e: Exception) {
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