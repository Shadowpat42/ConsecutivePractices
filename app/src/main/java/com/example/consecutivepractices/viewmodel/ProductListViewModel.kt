package com.example.consecutivepractices.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consecutivepractices.di.AppContainer
import com.example.consecutivepractices.domain.model.Product
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ProductListViewModel : ViewModel() {
    var uiState by mutableStateOf<ProductUiState<List<Product>>>(ProductUiState.Loading)
        private set

    private val badgeCache = AppContainer.filterBadgeCache
    val hasActiveFilters = badgeCache.hasActiveFilters

    private var loadJob: Job? = null

    init {
        loadProducts()
    }

    fun loadProducts() {
        loadJob?.cancel()

        uiState = ProductUiState.Loading
        loadJob = viewModelScope.launch {
            try {
                val filter = AppContainer.filters.load()
                badgeCache.update(filter.hasActiveFilters)
                val products = AppContainer.getProducts()
                uiState = ProductUiState.Success(products.filter(filter::matches))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                uiState = ProductUiState.Error("Не удалось загрузить товары. Попробуйте ещё раз.")
            }
        }
    }
}
