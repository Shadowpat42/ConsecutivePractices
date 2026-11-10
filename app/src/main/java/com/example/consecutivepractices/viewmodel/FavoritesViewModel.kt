package com.example.consecutivepractices.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consecutivepractices.di.AppContainer
import com.example.consecutivepractices.domain.model.Product
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {
    var uiState by mutableStateOf<ProductUiState<List<Product>>>(ProductUiState.Loading)
        private set

    init {
        load()
    }

    fun load() {
        uiState = ProductUiState.Loading
        viewModelScope.launch {
            AppContainer.favorites.observeAll()
                .catch { uiState = ProductUiState.Error("Не удалось прочитать избранное.") }
                .collect { uiState = ProductUiState.Success(it) }
        }
    }
}
