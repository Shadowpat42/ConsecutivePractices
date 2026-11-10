package com.example.consecutivepractices

import com.example.consecutivepractices.domain.reminder.formatLessonTime
import com.example.consecutivepractices.domain.reminder.lessonTimeToday
import com.example.consecutivepractices.domain.reminder.validateTime
import java.util.Calendar
import java.util.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LessonTimeTest {
    @Test
    fun acceptsOnlyValidHoursAndMinutes() {
        listOf("09:30", "23:59", "00:00").forEach { assertTrue(it, validateTime(it)) }
        listOf("", "9:30", "24:00", "12:60", "hello", "25:99").forEach {
            assertFalse(it, validateTime(it))
        }
    }

    @Test
    fun formatsWithLeadingZeros() {
        assertEquals("09:05", formatLessonTime(9, 5))
    }

    @Test
    fun currentMinuteAndEarlierAreNotFuture() {
        val now = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            clear()
            set(2026, Calendar.NOVEMBER, 10, 14, 30, 15)
        }

        assertTrue(lessonTimeToday("14:29", now) <= now.timeInMillis)
        assertTrue(lessonTimeToday("14:30", now) <= now.timeInMillis)
        assertTrue(lessonTimeToday("00:00", now) <= now.timeInMillis)
        assertEquals(45_000L, lessonTimeToday("14:31", now) - now.timeInMillis)
    }
}
