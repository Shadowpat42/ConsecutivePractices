package com.example.consecutivepractices.di

import com.example.consecutivepractices.data.remote.RetrofitClient
import com.example.consecutivepractices.data.repository.ProductRepositoryImpl
import com.example.consecutivepractices.domain.usecase.GetProductByIdUseCase
import com.example.consecutivepractices.domain.usecase.GetProductsUseCase

object AppContainer {
    private val repository = ProductRepositoryImpl(RetrofitClient.api)

    val getProducts = GetProductsUseCase(repository)
    val getProductById = GetProductByIdUseCase(repository)
}
