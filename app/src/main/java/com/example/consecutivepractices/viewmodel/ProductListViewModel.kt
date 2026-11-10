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

    private var loadJob: Job? = null

    init {
        loadProducts()
    }

    fun loadProducts() {
        if (loadJob?.isActive == true) return

        uiState = ProductUiState.Loading
        loadJob = viewModelScope.launch {
            try {
                uiState = ProductUiState.Success(AppContainer.getProducts())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                uiState = ProductUiState.Error("Не удалось загрузить товары. Попробуйте ещё раз.")
            }
        }
    }
}
