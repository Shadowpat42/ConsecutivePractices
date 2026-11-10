package com.example.consecutivepractices.domain.usecase

import com.example.consecutivepractices.domain.reminder.LessonReminderScheduler

class ScheduleLessonReminderUseCase(private val scheduler: LessonReminderScheduler) {
    operator fun invoke(triggerAtMillis: Long, fullName: String): Boolean =
        scheduler.schedule(triggerAtMillis, fullName)

    fun cancel() = scheduler.cancel()
}
