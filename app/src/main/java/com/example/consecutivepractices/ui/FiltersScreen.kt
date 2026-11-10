package com.example.consecutivepractices.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.consecutivepractices.viewmodel.FiltersViewModel

private val categories = listOf(null, "electronics", "jewelery", "men's clothing", "women's clothing")

@Composable
fun FiltersScreen(onDone: () -> Unit, onBack: () -> Unit) {
    val viewModel: FiltersViewModel = viewModel()
    var showCategories by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.saved) {
        if (viewModel.saved) onDone()
    }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onBack, enabled = !viewModel.isSaving) { Text("← Назад") }
        Text("Фильтры", style = MaterialTheme.typography.headlineMedium)

        val filter = viewModel.filter
        val error = viewModel.error
        if (filter == null) {
            if (error == null) ProductLoading()
            else ProductLoadError(error, viewModel::load)
            return@Column
        }

        Text("Категория")
        Column {
            OutlinedButton(onClick = { showCategories = true }, enabled = !viewModel.isSaving) {
                Text(filter.category ?: "Все")
            }
            DropdownMenu(expanded = showCategories, onDismissRequest = { showCategories = false }) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category ?: "Все") },
                        onClick = {
                            viewModel.selectCategory(category)
                            showCategories = false
                        }
                    )
                }
            }
        }

        Text("Минимальный рейтинг: ${filter.minRating.toInt()}")
        Slider(
            value = filter.minRating,
            onValueChange = viewModel::setMinRating,
            valueRange = 0f..5f,
            steps = 4,
            enabled = !viewModel.isSaving,
            modifier = Modifier.fillMaxWidth()
        )

        if (error != null) Text(error, color = MaterialTheme.colorScheme.error)
        Button(onClick = viewModel::save, enabled = !viewModel.isSaving) { Text("Готово") }
    }
}
