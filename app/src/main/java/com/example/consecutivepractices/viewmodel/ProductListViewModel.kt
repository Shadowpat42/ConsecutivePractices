package com.example.consecutivepractices.viewmodel

import androidx.lifecycle.ViewModel
import com.example.consecutivepractices.model.MockProducts

class ProductListViewModel : ViewModel() {

    val products = MockProducts.products
}