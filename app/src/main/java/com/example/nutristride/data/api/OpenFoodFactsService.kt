package com.example.nutristride.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenFoodFactsService {
    @GET("api/v2/product/{barcode}")
    suspend fun getProductByBarcode(@Path("barcode") barcode: String): OpenFoodFactsResponse
    
    @GET("cgi/search.pl")
    suspend fun searchProducts(
        @Query("search_terms") query: String,
        @Query("json") json: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): OpenFoodFactsSearchResponse
}