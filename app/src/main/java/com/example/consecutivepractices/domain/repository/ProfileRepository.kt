package com.example.consecutivepractices.domain.repository

import com.example.consecutivepractices.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observe(): Flow<UserProfile>
    suspend fun save(profile: UserProfile)
}
