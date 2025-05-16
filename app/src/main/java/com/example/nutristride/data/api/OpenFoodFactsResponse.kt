package com.example.nutristride.data.api

import com.google.gson.annotations.SerializedName

data class OpenFoodFactsResponse(
    val code: String,
    val product: Product,
    val status: Int
)

data class Product(
    val id: String,
    @SerializedName("product_name") val productName: String,
    val brands: String?,
    @SerializedName("nutriments") val nutrients: Nutrients,
    @SerializedName("serving_size") val servingSize: String?
)

data class Nutrients(
    @SerializedName("energy-kcal_100g") val calories: Float?,
    @SerializedName("proteins_100g") val protein: Float?,
    @SerializedName("carbohydrates_100g") val carbs: Float?,
    @SerializedName("fat_100g") val fat: Float?
)

data class OpenFoodFactsSearchResponse(
    val count: Int,
    val page: Int,
    @SerializedName("page_size") val pageSize: Int,
    val products: List<Product>
)