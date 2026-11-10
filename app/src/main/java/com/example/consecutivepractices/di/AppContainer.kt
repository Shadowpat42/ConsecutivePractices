package com.example.consecutivepractices.di

import android.content.Context
import androidx.room.Room
import com.example.consecutivepractices.data.platform.notification.AlarmManagerLessonReminderScheduler
import com.example.consecutivepractices.domain.usecase.ScheduleLessonReminderUseCase
import com.example.consecutivepractices.data.local.database.AppDatabase
import com.example.consecutivepractices.data.local.preferences.FilterPreferencesDataSource
import com.example.consecutivepractices.data.local.preferences.ProfilePreferencesDataSource
import com.example.consecutivepractices.data.platform.ProfileImageManager
import com.example.consecutivepractices.data.platform.ResumeDownloader
import com.example.consecutivepractices.data.remote.RetrofitClient
import com.example.consecutivepractices.data.repository.ProfileRepositoryImpl
import com.example.consecutivepractices.domain.usecase.GetProfileUseCase
import com.example.consecutivepractices.domain.usecase.SaveProfileUseCase
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

    lateinit var getProfile: GetProfileUseCase
        private set
    lateinit var saveProfile: SaveProfileUseCase
        private set
    lateinit var profileImages: ProfileImageManager
        private set
    lateinit var resumeDownloader: ResumeDownloader
        private set

    lateinit var scheduleLessonReminder: ScheduleLessonReminderUseCase
        private set

    fun initialize(context: Context) {
        val appContext = context.applicationContext
        val profileRepository = ProfileRepositoryImpl(ProfilePreferencesDataSource(appContext))
        getProfile = GetProfileUseCase(profileRepository)
        saveProfile = SaveProfileUseCase(profileRepository)
        profileImages = ProfileImageManager(appContext)
        resumeDownloader = ResumeDownloader(appContext)
        scheduleLessonReminder = ScheduleLessonReminderUseCase(
            AlarmManagerLessonReminderScheduler(appContext)
        )
        filters = FilterRepositoryImpl(FilterPreferencesDataSource(context))
        val database = Room.databaseBuilder(
            context.applicationContext, AppDatabase::class.java, "favorites.db"
        ).build()
        favorites = FavoritesRepositoryImpl(database.favoriteProductDao())
    }
}
