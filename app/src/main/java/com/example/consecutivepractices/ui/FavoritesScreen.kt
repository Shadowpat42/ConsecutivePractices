package com.example.consecutivepractices.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.consecutivepractices.viewmodel.FavoritesViewModel
import com.example.consecutivepractices.viewmodel.ProductUiState

@Composable
fun FavoritesScreen() {
    val viewModel: FavoritesViewModel = viewModel()
    when (val state = viewModel.uiState) {
        ProductUiState.Loading -> ProductLoading()
        is ProductUiState.Error -> ProductLoadError(state.message, viewModel::load)
        is ProductUiState.Success -> ProductList(
            products = state.data,
            title = "Избранное",
            emptyMessage = "В избранном пока нет товаров."
        )
    }
}
