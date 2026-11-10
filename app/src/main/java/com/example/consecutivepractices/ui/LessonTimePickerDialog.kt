package com.example.consecutivepractices.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.consecutivepractices.domain.reminder.validateTime
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LessonTimePickerDialog(
    time: String,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    val now = Calendar.getInstance()
    val valid = validateTime(time)
    val state = rememberTimePickerState(
        initialHour = if (valid) time.substring(0, 2).toInt() else now.get(Calendar.HOUR_OF_DAY),
        initialMinute = if (valid) time.substring(3).toInt() else now.get(Calendar.MINUTE),
        is24Hour = true
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.extraLarge) {
            Column(
                modifier = Modifier.widthIn(max = 360.dp).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Время любимой пары", style = MaterialTheme.typography.titleMedium)
                TimePicker(state = state)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Отмена") }
                    TextButton(onClick = { onConfirm(state.hour, state.minute) }) {
                        Text("Выбрать")
                    }
                }
            }
        }
    }
}
