package com.example.news.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.news.domain.entity.Language
import com.example.news.domain.entity.Settings
import com.example.news.domain.entity.toPeriod
import com.example.news.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    val languageKey = stringPreferencesKey("language")
    val periodKey = intPreferencesKey("period")
    val wifiOnlyKey = booleanPreferencesKey("wifi")
    val notificationsKey = booleanPreferencesKey("notifications")

    override fun getSettings(): Flow<Settings> {
        return context.dataStore.data.map {preferences ->

            val languageAsString = preferences[languageKey] ?: Settings.defaultEnglish.name
            val language = Language.valueOf(languageAsString)
            val periodAsInt = preferences[periodKey] ?: Settings.defaultPeriod.minutes
            val period = periodAsInt.toPeriod()
            val wifiOnly = preferences[wifiOnlyKey] ?: Settings.DEFAULT_WIFI_ONLY
            val notifications = preferences[notificationsKey] ?: Settings.DEFAULT_NOTIFICATIONS

            Settings(
                language = language,
                period = period,
                showNotifications = notifications,
                wifiOnly = wifiOnly
            )
        }

    }

    override suspend fun updateLanguage(language: Language) {
        context.dataStore.edit {
            it[languageKey] = language.name
        }
    }

    override suspend fun updatePeriod(period: Int) {
        context.dataStore.edit {
            it[periodKey] = period
        }
    }

    override suspend fun updateWifiOnly(wifiOnly: Boolean) {
        context.dataStore.edit {
            it[wifiOnlyKey] = wifiOnly
        }
    }

    override suspend fun updateNotifications(notifications: Boolean) {
       context.dataStore.edit {
           it[notificationsKey] = notifications
       }
    }
}