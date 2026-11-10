package com.example.consecutivepractices.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteProductEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteProductDao(): FavoriteProductDao
}
