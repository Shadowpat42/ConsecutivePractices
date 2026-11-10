package com.example.consecutivepractices.domain.usecase

import com.example.consecutivepractices.domain.repository.ProductRepository

class GetProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(limit: Int = 10) = repository.getProducts(limit)
}
