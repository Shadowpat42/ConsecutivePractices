package com.example.consecutivepractices.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.example.consecutivepractices.R
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
    onProductClick: (Int) -> Unit,
    onFiltersClick: () -> Unit,
    filtersSaved: Boolean,
    onFiltersHandled: () -> Unit
) {
    val viewModel: ProductListViewModel = viewModel()
    val hasActiveFilters by viewModel.hasActiveFilters.collectAsState()

    LaunchedEffect(filtersSaved) {
        if (filtersSaved) {
            viewModel.loadProducts()
            onFiltersHandled()
        }
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Товары", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = onFiltersClick) {
                BadgedBox(badge = {
                    if (hasActiveFilters) {
                        Badge(Modifier.semantics { contentDescription = "Фильтры активны" })
                    }
                }) {
                    Text("Фильтры")
                }
            }
        }

        when (val state = viewModel.uiState) {
            ProductUiState.Loading -> ProductLoading()
            is ProductUiState.Success -> ProductList(
                products = state.data,
                emptyMessage = "Нет товаров по выбранным фильтрам.",
                onProductClick = onProductClick
            )
            is ProductUiState.Error -> ProductLoadError(state.message, viewModel::loadProducts)
        }
    }
}

@Composable
internal fun ProductList(
    products: List<Product>,
    emptyMessage: String,
    title: String? = null,
    onProductClick: ((Int) -> Unit)? = null
) {
    LazyColumn {
        if (title != null) {
            item {
                Text(title, Modifier.padding(16.dp), fontSize = 30.sp, fontWeight = FontWeight.Bold)
            }
        }
        if (products.isEmpty()) {
            item { Text(emptyMessage, Modifier.padding(16.dp)) }
        }

        items(products, key = { it.id }) { product ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 6.dp
                    )
                    .then(
                        if (onProductClick == null) Modifier
                        else Modifier.clickable { onProductClick(product.id) }
                    )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    AsyncImage(
                        model = product.image,
                        contentDescription = null,
                        placeholder = painterResource(R.drawable.ic_product_placeholder),
                        error = painterResource(R.drawable.ic_product_placeholder),
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
