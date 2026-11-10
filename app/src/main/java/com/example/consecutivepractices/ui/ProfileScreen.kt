package com.example.consecutivepractices.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.consecutivepractices.viewmodel.ProductUiState
import com.example.consecutivepractices.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(onEdit: () -> Unit) {
    val viewModel: ProfileViewModel = viewModel()
    val context = LocalContext.current

    val message = viewModel.message
    LaunchedEffect(message) {
        if (message != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.messageShown()
        }
    }

    val resume = viewModel.downloadedResume
    LaunchedEffect(resume) {
        if (resume != null) {
            val intent = Intent(Intent.ACTION_VIEW)
                .setDataAndType(resume.uri, resume.mimeType)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "Нет приложения для открытия файла", Toast.LENGTH_LONG).show()
            } catch (e: SecurityException) {
                Toast.makeText(context, "Не удалось открыть файл", Toast.LENGTH_LONG).show()
            } finally {
                viewModel.resumeHandled()
            }
        }
    }

    when (val state = viewModel.uiState) {
        ProductUiState.Loading -> ProductLoading()
        is ProductUiState.Error -> ProductLoadError(state.message, viewModel::load)
        is ProductUiState.Success -> {
            val profile = state.data
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Профиль", style = MaterialTheme.typography.headlineMedium)
                ProfileAvatar(profile.avatarUri)
                Text(
                    profile.fullName.ifBlank { "Профиль не заполнен" },
                    style = MaterialTheme.typography.titleLarge
                )
                if (profile.position.isNotBlank()) Text(profile.position)
                Button(onClick = onEdit) { Text("Редактировать") }
                if (profile.resumeUrl.isNotBlank()) {
                    OutlinedButton(
                        onClick = viewModel::downloadResume,
                        enabled = !viewModel.isDownloading
                    ) {
                        Text(if (viewModel.isDownloading) "Скачивание..." else "Резюме")
                    }
                }
            }
        }
    }
}
