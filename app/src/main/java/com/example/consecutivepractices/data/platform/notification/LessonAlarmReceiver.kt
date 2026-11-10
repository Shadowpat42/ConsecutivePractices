package com.example.consecutivepractices.data.platform.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class LessonAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        NotificationHelper.show(context, intent.getStringExtra(EXTRA_FULL_NAME).orEmpty())
    }

    companion object {
        const val EXTRA_FULL_NAME = "lesson_full_name"
    }
}
