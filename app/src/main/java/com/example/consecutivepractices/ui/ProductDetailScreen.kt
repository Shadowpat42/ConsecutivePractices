package com.example.consecutivepractices.ui

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.consecutivepractices.viewmodel.ProductDetailViewModel

@Composable
fun ProductDetailScreen(
    productId: Int,
    onBackClick: () -> Unit
) {

    val viewModel: ProductDetailViewModel = viewModel()

    val product = viewModel.getProductById(productId)

    if (product == null) {
        Text(
            text = "Товар не найден",
            modifier = Modifier.padding(16.dp)
        )

        return
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        TextButton(
            onClick = onBackClick
        ) {
            Text("← Назад")
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

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

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🛍",
                    fontSize = 70.sp
                )
            }
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