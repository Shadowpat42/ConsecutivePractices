package com.example.consecutivepractices.domain.reminder

import java.util.Calendar
import java.util.Locale

private val lessonTimePattern = Regex("([01][0-9]|2[0-3]):[0-5][0-9]")

fun validateTime(value: String): Boolean = lessonTimePattern.matches(value)

fun formatLessonTime(hour: Int, minute: Int): String =
    String.format(Locale.ROOT, "%02d:%02d", hour, minute)

fun lessonTimeToday(value: String, now: Calendar): Long {
    require(validateTime(value))
    return (now.clone() as Calendar).apply {
        set(Calendar.HOUR_OF_DAY, value.substring(0, 2).toInt())
        set(Calendar.MINUTE, value.substring(3).toInt())
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}
