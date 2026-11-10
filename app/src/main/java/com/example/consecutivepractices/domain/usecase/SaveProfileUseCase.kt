package com.example.consecutivepractices.domain.usecase

import com.example.consecutivepractices.domain.model.UserProfile
import com.example.consecutivepractices.domain.repository.ProfileRepository

class SaveProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(profile: UserProfile) = repository.save(profile)
}
