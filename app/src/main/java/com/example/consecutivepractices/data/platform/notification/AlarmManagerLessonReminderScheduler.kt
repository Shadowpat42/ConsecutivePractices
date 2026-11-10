package com.example.consecutivepractices.data.platform.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.consecutivepractices.domain.reminder.LessonReminderScheduler

class AlarmManagerLessonReminderScheduler(private val context: Context) : LessonReminderScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(triggerAtMillis: Long, fullName: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            return false
        }

        return try {
            cancel()
            val intent = alarmIntent().putExtra(LessonAlarmReceiver.EXTRA_FULL_NAME, fullName)
            val pendingIntent = PendingIntent.getBroadcast(
                context, REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent
            )
            true
        } catch (e: SecurityException) {
            false
        }
    }

    override fun cancel() {
        val pendingIntent = PendingIntent.getBroadcast(
            context, REQUEST_CODE, alarmIntent(),
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) alarmManager.cancel(pendingIntent)
    }

    private fun alarmIntent() = Intent(context, LessonAlarmReceiver::class.java)

    private companion object {
        const val REQUEST_CODE = 701
    }
}
