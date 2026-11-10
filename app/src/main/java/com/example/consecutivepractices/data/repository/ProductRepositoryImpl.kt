package com.example.consecutivepractices.data.repository

import com.example.consecutivepractices.data.remote.FakeStoreApi
import com.example.consecutivepractices.data.remote.toDomain
import com.example.consecutivepractices.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(private val api: FakeStoreApi) : ProductRepository {
    override suspend fun getProducts(limit: Int) = withContext(Dispatchers.IO) {
        api.getProducts(limit).map { it.toDomain() }
    }

    override suspend fun getProductById(id: Int) = withContext(Dispatchers.IO) {
        checkNotNull(api.getProductById(id)).toDomain()
    }
}
