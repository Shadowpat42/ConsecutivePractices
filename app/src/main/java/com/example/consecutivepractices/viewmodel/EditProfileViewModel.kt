package com.example.consecutivepractices.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consecutivepractices.di.AppContainer
import com.example.consecutivepractices.domain.model.UserProfile
import com.example.consecutivepractices.domain.reminder.formatLessonTime
import com.example.consecutivepractices.domain.reminder.lessonTimeToday
import com.example.consecutivepractices.domain.reminder.validateTime
import java.io.IOException
import java.util.Calendar
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
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

    var needsExactAlarmAccess by mutableStateOf(false)
        private set
    var saveNotice by mutableStateOf<String?>(null)
        private set
    private var nowMillis by mutableStateOf(System.currentTimeMillis())

    val lessonTimeError: String?
        get() = profile?.let { timeError(it.favoriteLessonTime, nowMillis) }

    init {
        load()
        viewModelScope.launch {
            while (isActive) {
                nowMillis = System.currentTimeMillis()
                delay(1_000)
            }
        }
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

    fun setFavoriteLessonTime(value: String) {
        profile = profile?.copy(favoriteLessonTime = value)
        error = null
    }

    fun chooseLessonTime(hour: Int, minute: Int) {
        setFavoriteLessonTime(formatLessonTime(hour, minute))
    }

    fun dismissExactAlarmAccess() {
        needsExactAlarmAccess = false
    }

    private fun timeError(value: String, now: Long): String? {
        if (!validateTime(value)) return "Введите время в формате HH:mm"
        val today = Calendar.getInstance().apply { timeInMillis = now }
        return if (lessonTimeToday(value, today) <= now) "Это время сегодня уже прошло" else null
    }

    fun save(notificationsAllowed: Boolean) {
        val current = profile ?: return
        if (isSaving || isPreparingImage || saved) return
        val now = Calendar.getInstance()
        nowMillis = now.timeInMillis
        if (timeError(current.favoriteLessonTime, nowMillis) != null) return
        val triggerAtMillis = lessonTimeToday(current.favoriteLessonTime, now)
        val fullName = current.fullName.trim()
        isSaving = true
        error = null
        viewModelScope.launch {
            try {
                AppContainer.saveProfile(current.copy(
                    fullName = fullName,
                    position = current.position.trim(),
                    resumeUrl = current.resumeUrl.trim()
                ))
                val reminder = AppContainer.scheduleLessonReminder
                nowMillis = System.currentTimeMillis()
                if (triggerAtMillis <= nowMillis) return@launch

                if (!notificationsAllowed) {
                    reminder.cancel()
                    saveNotice = "Профиль сохранён. Без разрешения уведомление не появится."
                } else if (!reminder(triggerAtMillis, fullName)) {
                    error = "Напоминание не установлено. Разрешите точные будильники и снова нажмите «Готово»."
                    needsExactAlarmAccess = true
                    return@launch
                }
                saved = true
            } catch (e: IOException) {
                error = "Не удалось сохранить профиль."
            } finally {
                isSaving = false
            }
        }
    }
}
