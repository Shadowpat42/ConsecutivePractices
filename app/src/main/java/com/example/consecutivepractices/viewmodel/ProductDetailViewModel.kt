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

class ProductDetailViewModel : ViewModel() {
    var uiState by mutableStateOf<ProductUiState<Product>>(ProductUiState.Loading)
        private set

    private var loadedId: Int? = null
    private var loadJob: Job? = null

    fun loadProduct(id: Int) {
        if (loadedId == id) return
        loadedId = id
        requestProduct(id)
    }

    fun retry() {
        loadedId?.let { requestProduct(it) }
    }

    private fun requestProduct(id: Int) {
        loadJob?.cancel()
        uiState = ProductUiState.Loading
        loadJob = viewModelScope.launch {
            try {
                uiState = ProductUiState.Success(AppContainer.getProductById(id))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                uiState = ProductUiState.Error("Не удалось загрузить товар. Попробуйте ещё раз.")
            }
        }
    }
}
