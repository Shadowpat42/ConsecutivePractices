package com.example.consecutivepractices.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consecutivepractices.di.AppContainer
import com.example.consecutivepractices.domain.model.ProductFilter
import java.io.IOException
import kotlinx.coroutines.launch

class FiltersViewModel : ViewModel() {
    private val repository = AppContainer.filters
    private val badgeCache = AppContainer.filterBadgeCache

    var filter by mutableStateOf<ProductFilter?>(null)
        private set
    var isSaving by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set
    var saved by mutableStateOf(false)
        private set

    init {
        load()
    }

    fun load() {
        error = null
        viewModelScope.launch {
            try {
                filter = repository.load()
            } catch (e: IOException) {
                error = "Не удалось прочитать фильтры."
            }
        }
    }

    fun selectCategory(category: String?) {
        filter = filter?.copy(category = category)
    }

    fun setMinRating(rating: Float) {
        filter = filter?.copy(minRating = rating)
    }

    fun save() {
        val current = filter ?: return
        if (isSaving || saved) return
        isSaving = true
        error = null
        viewModelScope.launch {
            try {
                repository.save(current)
                badgeCache.update(current.hasActiveFilters)
                saved = true
            } catch (e: IOException) {
                error = "Не удалось сохранить фильтры."
            } finally {
                isSaving = false
            }
        }
    }
}
