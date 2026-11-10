package com.example.consecutivepractices.domain.repository

import com.example.consecutivepractices.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(limit: Int): List<Product>
    suspend fun getProductById(id: Int): Product
}
