package com.example.consecutivepractices.domain.usecase

import com.example.consecutivepractices.domain.repository.ProfileRepository

class GetProfileUseCase(private val repository: ProfileRepository) {
    operator fun invoke() = repository.observe()
}
