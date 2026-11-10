package com.example.consecutivepractices.viewmodel

sealed interface ProductUiState<out T> {
    data object Loading : ProductUiState<Nothing>
    data class Success<T>(val data: T) : ProductUiState<T>
    data class Error(val message: String) : ProductUiState<Nothing>
}
