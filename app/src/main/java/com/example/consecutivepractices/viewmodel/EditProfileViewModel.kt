package com.example.consecutivepractices.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consecutivepractices.di.AppContainer
import com.example.consecutivepractices.domain.model.UserProfile
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class EditProfileViewModel : ViewModel() {
    private val images = AppContainer.profileImages
    private var loadJob: Job? = null

    var profile by mutableStateOf<UserProfile?>(null)
        private set
    var isSaving by mutableStateOf(false)
        private set
    var isPreparingImage by mutableStateOf(false)
        private set
    var saved by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    init {
        load()
    }

    fun load() {
        error = null
        loadJob = viewModelScope.launch {
            try {
                profile = AppContainer.getProfile().first()
            } catch (e: IOException) {
                error = "Не удалось прочитать профиль."
            }
        }
    }

    fun setFullName(value: String) {
        profile = profile?.copy(fullName = value)
    }

    fun setPosition(value: String) {
        profile = profile?.copy(position = value)
    }

    fun setResumeUrl(value: String) {
        profile = profile?.copy(resumeUrl = value)
    }

    fun useGalleryImage(uri: Uri) {
        isPreparingImage = true
        error = null
        viewModelScope.launch {
            try {
                val image = images.keepGalleryImage(uri)
                loadJob?.join()
                profile = profile?.copy(avatarUri = image.toString())
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                error = "Не удалось сохранить выбранное изображение."
            } finally {
                isPreparingImage = false
            }
        }
    }

    suspend fun createCameraImage(): Uri? {
        error = null
        isPreparingImage = true
        return try {
            images.createCameraImage()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            error = "Не удалось подготовить файл для фотографии."
            null
        } finally {
            isPreparingImage = false
        }
    }

    fun useCameraImage(uri: String) {
        viewModelScope.launch {
            loadJob?.join()
            profile = profile?.copy(avatarUri = uri)
        }
    }

    fun cameraUnavailable() {
        error = "Не удалось открыть приложение камеры."
    }

    fun save() {
        val current = profile ?: return
        if (isSaving || isPreparingImage || saved) return
        isSaving = true
        error = null
        viewModelScope.launch {
            try {
                AppContainer.saveProfile(current.copy(
                    fullName = current.fullName.trim(),
                    position = current.position.trim(),
                    resumeUrl = current.resumeUrl.trim()
                ))
                saved = true
            } catch (e: IOException) {
                error = "Не удалось сохранить профиль."
            } finally {
                isSaving = false
            }
        }
    }
}
