package com.example.consecutivepractices.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.consecutivepractices.domain.model.Product
import com.example.consecutivepractices.viewmodel.ProductListViewModel
import com.example.consecutivepractices.viewmodel.ProductUiState

@Composable
fun ProductListScreen(
    onProductClick: (Int) -> Unit
) {

    val viewModel: ProductListViewModel = viewModel()

    when (val state = viewModel.uiState) {
        ProductUiState.Loading -> ProductLoading()
        is ProductUiState.Success -> ProductList(state.data, onProductClick)
        is ProductUiState.Error -> ProductLoadError(state.message, viewModel::loadProducts)
    }
}

@Composable
private fun ProductList(products: List<Product>, onProductClick: (Int) -> Unit) {

    LazyColumn {

        item {
            Text(
                text = "Товары",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }

        items(products, key = { it.id }) { product ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 6.dp
                    )
                    .clickable {
                        onProductClick(product.id)
                    }
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    AsyncImage(
                        model = product.image,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().height(120.dp)
                    )

                    Text(
                        text = product.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = product.category
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = "Цена: ${product.price} $"
                    )

                    Text(
                        text = "Рейтинг: ${product.rating.rate}"
                    )
                }
            }
        }
    }
}