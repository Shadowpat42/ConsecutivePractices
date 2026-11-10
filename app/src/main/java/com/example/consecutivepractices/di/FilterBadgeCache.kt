package com.example.consecutivepractices.di

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FilterBadgeCache {
    private val active = MutableStateFlow(false)
    val hasActiveFilters = active.asStateFlow()

    fun update(hasActiveFilters: Boolean) {
        active.value = hasActiveFilters
    }
}
