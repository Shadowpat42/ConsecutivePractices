package com.example.consecutivepractices.data.remote

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient

internal fun createHttpClient(context: Context): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(ChuckerInterceptor(context))
    .build()
