package com.example.consecutivepractices.domain.usecase

import com.example.consecutivepractices.domain.repository.ProductRepository

class GetProductByIdUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(id: Int) = repository.getProductById(id)
}
