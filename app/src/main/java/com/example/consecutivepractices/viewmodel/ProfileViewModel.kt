package com.example.consecutivepractices.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consecutivepractices.data.platform.DownloadedResume
import com.example.consecutivepractices.di.AppContainer
import com.example.consecutivepractices.domain.model.UserProfile
import com.example.consecutivepractices.domain.model.isValidResumeUrl
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    var uiState by mutableStateOf<ProductUiState<UserProfile>>(ProductUiState.Loading)
        private set
    var isDownloading by mutableStateOf(false)
        private set
    var downloadedResume by mutableStateOf<DownloadedResume?>(null)
        private set
    var message by mutableStateOf<String?>(null)
        private set

    init {
        load()
    }

    fun load() {
        uiState = ProductUiState.Loading
        viewModelScope.launch {
            AppContainer.getProfile()
                .catch { uiState = ProductUiState.Error("Не удалось прочитать профиль.") }
                .collect { uiState = ProductUiState.Success(it) }
        }
    }

    fun downloadResume() {
        if (isDownloading) return
        val url = (uiState as? ProductUiState.Success)?.data?.resumeUrl.orEmpty()
        if (!isValidResumeUrl(url)) {
            message = "Укажите корректную ссылку, начинающуюся с http:// или https://."
            return
        }

        isDownloading = true
        viewModelScope.launch {
            try {
                downloadedResume = AppContainer.resumeDownloader.download(url)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                message = "Не удалось скачать резюме. Проверьте ссылку и подключение."
            } finally {
                isDownloading = false
            }
        }
    }

    fun resumeHandled() {
        downloadedResume = null
    }

    fun messageShown() {
        message = null
    }
}
