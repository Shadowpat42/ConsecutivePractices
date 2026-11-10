package com.example.consecutivepractices.data.repository

import com.example.consecutivepractices.data.local.preferences.ProfilePreferencesDataSource
import com.example.consecutivepractices.domain.model.UserProfile
import com.example.consecutivepractices.domain.repository.ProfileRepository

class ProfileRepositoryImpl(private val source: ProfilePreferencesDataSource) : ProfileRepository {
    override fun observe() = source.observe()

    override suspend fun save(profile: UserProfile) = source.save(profile)
}
