package com.example.consecutivepractices.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.consecutivepractices.domain.model.Product
import com.example.consecutivepractices.viewmodel.ProductDetailViewModel
import com.example.consecutivepractices.viewmodel.ProductUiState

@Composable
fun ProductDetailScreen(
    productId: Int,
    onBackClick: () -> Unit
) {

    val viewModel: ProductDetailViewModel = viewModel()

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        TextButton(onClick = onBackClick) {
            Text("← Назад")
        }

        when (val state = viewModel.uiState) {
            ProductUiState.Loading -> ProductLoading()
            is ProductUiState.Success -> ProductDetails(state.data)
            is ProductUiState.Error -> ProductLoadError(state.message, viewModel::retry)
        }
    }
}

@Composable
private fun ProductDetails(product: Product) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

        Text(
            text = product.title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {

            AsyncImage(
                model = product.image,
                contentDescription = product.title,
                modifier = Modifier.fillMaxWidth().height(200.dp).padding(8.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Информация",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "Категория: ${product.category}"
                )

                Text(
                    text = "Цена: ${product.price} $"
                )

                Text(
                    text = "Рейтинг: ${product.rating.rate}"
                )

                Text(
                    text = "Оценок: ${product.rating.count}"
                )
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Описание",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = product.description,
            fontSize = 17.sp
        )
    }
}