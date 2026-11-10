package com.example.consecutivepractices.domain.reminder

interface LessonReminderScheduler {
    fun schedule(triggerAtMillis: Long, fullName: String): Boolean
    fun cancel()
}
