package com.example.consecutivepractices.di

import android.content.Context
import androidx.room.Room
import com.example.consecutivepractices.data.local.database.AppDatabase
import com.example.consecutivepractices.data.local.preferences.FilterPreferencesDataSource
import com.example.consecutivepractices.data.remote.RetrofitClient
import com.example.consecutivepractices.data.repository.FavoritesRepositoryImpl
import com.example.consecutivepractices.data.repository.FilterRepositoryImpl
import com.example.consecutivepractices.data.repository.ProductRepositoryImpl
import com.example.consecutivepractices.domain.repository.FavoritesRepository
import com.example.consecutivepractices.domain.repository.FilterRepository
import com.example.consecutivepractices.domain.usecase.GetProductByIdUseCase
import com.example.consecutivepractices.domain.usecase.GetProductsUseCase

object AppContainer {
    private val repository = ProductRepositoryImpl(RetrofitClient.api)

    val getProducts = GetProductsUseCase(repository)
    val getProductById = GetProductByIdUseCase(repository)
    val filterBadgeCache = FilterBadgeCache()

    lateinit var filters: FilterRepository
        private set
    lateinit var favorites: FavoritesRepository
        private set

    fun initialize(context: Context) {
        filters = FilterRepositoryImpl(FilterPreferencesDataSource(context))
        val database = Room.databaseBuilder(
            context.applicationContext, AppDatabase::class.java, "favorites.db"
        ).build()
        favorites = FavoritesRepositoryImpl(database.favoriteProductDao())
    }
}
