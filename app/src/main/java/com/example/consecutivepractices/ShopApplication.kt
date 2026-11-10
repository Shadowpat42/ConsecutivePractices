package com.example.consecutivepractices

import android.app.Application
import com.example.consecutivepractices.data.remote.RetrofitClient
import com.example.consecutivepractices.di.AppContainer

class ShopApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RetrofitClient.initialize(this)
        AppContainer.initialize(this)
    }
}
