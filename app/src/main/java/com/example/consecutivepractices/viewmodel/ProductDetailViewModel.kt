package com.example.consecutivepractices.viewmodel

import androidx.lifecycle.ViewModel
import com.example.consecutivepractices.model.MockProducts
import com.example.consecutivepractices.model.Product

class ProductDetailViewModel : ViewModel() {

    fun getProductById(id: Int): Product? {
        return MockProducts.products.find { product ->
            product.id == id
        }
    }
}