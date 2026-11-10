package com.example.consecutivepractices.domain.repository

import com.example.consecutivepractices.domain.model.ProductFilter

interface FilterRepository {
    suspend fun load(): ProductFilter
    suspend fun save(filter: ProductFilter)
}
