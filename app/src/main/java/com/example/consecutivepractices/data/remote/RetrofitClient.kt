package com.example.consecutivepractices.data.remote

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    lateinit var api: FakeStoreApi
        private set

    fun initialize(context: Context) {
        api = Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .client(createHttpClient(context.applicationContext))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FakeStoreApi::class.java)
    }
}
