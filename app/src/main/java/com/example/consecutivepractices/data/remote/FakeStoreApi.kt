package com.example.consecutivepractices.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FakeStoreApi {
    @GET("products")
    suspend fun getProducts(@Query("limit") limit: Int): List<ProductDto>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductDto?
}
