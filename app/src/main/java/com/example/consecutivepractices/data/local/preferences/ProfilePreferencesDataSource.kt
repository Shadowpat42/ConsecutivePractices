package com.example.consecutivepractices.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.consecutivepractices.domain.model.UserProfile
import kotlinx.coroutines.flow.map

class ProfilePreferencesDataSource(context: Context) {
    private val dataStore = context.applicationContext.shopPreferences
    private val fullNameKey = stringPreferencesKey("profile_full_name")
    private val positionKey = stringPreferencesKey("profile_position")
    private val avatarKey = stringPreferencesKey("profile_avatar")
    private val resumeKey = stringPreferencesKey("profile_resume")
    private val lessonTimeKey = stringPreferencesKey("profile_favorite_lesson_time")

    fun observe() = dataStore.data.map { preferences ->
        UserProfile(
            fullName = preferences[fullNameKey].orEmpty(),
            position = preferences[positionKey].orEmpty(),
            avatarUri = preferences[avatarKey],
            resumeUrl = preferences[resumeKey].orEmpty(),
            favoriteLessonTime = preferences[lessonTimeKey].orEmpty()
        )
    }

    suspend fun save(profile: UserProfile) {
        dataStore.edit { preferences ->
            preferences[fullNameKey] = profile.fullName
            preferences[positionKey] = profile.position
            preferences[resumeKey] = profile.resumeUrl
            preferences[lessonTimeKey] = profile.favoriteLessonTime
            val avatar = profile.avatarUri
            if (avatar == null) preferences.remove(avatarKey)
            else preferences[avatarKey] = avatar
        }
    }
}
