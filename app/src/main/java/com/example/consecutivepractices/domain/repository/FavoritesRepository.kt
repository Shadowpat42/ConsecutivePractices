package com.example.consecutivepractices.domain.repository

import com.example.consecutivepractices.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeAll(): Flow<List<Product>>
    suspend fun contains(id: Int): Boolean
    suspend fun setFavorite(product: Product, favorite: Boolean)
}
