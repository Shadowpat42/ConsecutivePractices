package com.example.consecutivepractices.ui

import android.content.ActivityNotFoundException
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.consecutivepractices.viewmodel.EditProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(onBack: () -> Unit) {
    val viewModel: EditProfileViewModel = viewModel()
    val scope = rememberCoroutineScope()
    var showAvatarOptions by rememberSaveable { mutableStateOf(false) }
    var pendingPhoto by rememberSaveable { mutableStateOf<String?>(null) }

    val gallery = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) viewModel.useGalleryImage(uri)
    }
    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        val photo = pendingPhoto
        pendingPhoto = null
        if (success && photo != null) viewModel.useCameraImage(photo)
    }

    LaunchedEffect(viewModel.saved) {
        if (viewModel.saved) onBack()
    }

    if (showAvatarOptions) {
        AlertDialog(
            onDismissRequest = { showAvatarOptions = false },
            title = { Text("Выбрать аватар") },
            text = {
                Column {
                    TextButton(onClick = {
                        showAvatarOptions = false
                        gallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }) { Text("Галерея") }
                    TextButton(onClick = {
                        showAvatarOptions = false
                        scope.launch {
                            val uri = viewModel.createCameraImage() ?: return@launch
                            pendingPhoto = uri.toString()
                            try {
                                camera.launch(uri)
                            } catch (e: ActivityNotFoundException) {
                                pendingPhoto = null
                                viewModel.cameraUnavailable()
                            } catch (e: SecurityException) {
                                pendingPhoto = null
                                viewModel.cameraUnavailable()
                            }
                        }
                    }) { Text("Камера") }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAvatarOptions = false }) { Text("Отмена") }
            }
        )
    }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onBack, enabled = !viewModel.isSaving) { Text("← Назад") }
        Text("Редактировать профиль", style = MaterialTheme.typography.headlineSmall)

        val profile = viewModel.profile
        val error = viewModel.error
        if (profile == null) {
            if (error == null) ProductLoading() else ProductLoadError(error, viewModel::load)
            return@Column
        }

        val editable = !viewModel.isSaving
        ProfileAvatar(
            uri = profile.avatarUri,
            modifier = Modifier.clickable(
                enabled = editable && !viewModel.isPreparingImage,
                onClickLabel = "Выбрать аватар"
            ) { showAvatarOptions = true }
        )
        OutlinedTextField(
            profile.fullName, viewModel::setFullName, Modifier.fillMaxWidth(),
            label = { Text("ФИО") }, singleLine = true, enabled = editable
        )
        OutlinedTextField(
            profile.position, viewModel::setPosition, Modifier.fillMaxWidth(),
            label = { Text("Должность") }, singleLine = true, enabled = editable
        )
        OutlinedTextField(
            profile.resumeUrl, viewModel::setResumeUrl, Modifier.fillMaxWidth(),
            label = { Text("Ссылка на резюме") }, singleLine = true, enabled = editable,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
        )
        if (error != null) Text(error, color = MaterialTheme.colorScheme.error)
        Button(onClick = viewModel::save, enabled = editable && !viewModel.isPreparingImage) {
            Text("Готово")
        }
    }
}
