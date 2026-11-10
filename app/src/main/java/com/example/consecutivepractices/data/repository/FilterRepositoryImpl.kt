package com.example.consecutivepractices.data.repository

import com.example.consecutivepractices.data.local.preferences.FilterPreferencesDataSource
import com.example.consecutivepractices.domain.model.ProductFilter
import com.example.consecutivepractices.domain.repository.FilterRepository

class FilterRepositoryImpl(private val source: FilterPreferencesDataSource) : FilterRepository {
    override suspend fun load() = source.load()

    override suspend fun save(filter: ProductFilter) = source.save(filter)
}
