package com.example.consecutivepractices.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.consecutivepractices.domain.model.ProductFilter
import kotlinx.coroutines.flow.first

internal val Context.shopPreferences by preferencesDataStore(name = "product_filters")

class FilterPreferencesDataSource(context: Context) {
    private val dataStore = context.applicationContext.shopPreferences
    private val categoryKey = stringPreferencesKey("category")
    private val ratingKey = floatPreferencesKey("min_rating")

    suspend fun load(): ProductFilter {
        val preferences = dataStore.data.first()
        return ProductFilter(preferences[categoryKey], preferences[ratingKey] ?: 0f)
    }

    suspend fun save(filter: ProductFilter) {
        dataStore.edit { preferences ->
            val category = filter.category
            if (category == null) preferences.remove(categoryKey)
            else preferences[categoryKey] = category
            preferences[ratingKey] = filter.minRating
        }
    }
}
