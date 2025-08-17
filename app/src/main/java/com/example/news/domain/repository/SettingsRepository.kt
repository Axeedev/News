package com.example.news.domain.repository

import com.example.news.domain.entity.Language
import com.example.news.domain.entity.RefreshConfig
import com.example.news.domain.entity.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getSettings(): Flow<Settings>

    suspend fun updateLanguage(language: Language)
    suspend fun updatePeriod(period: Int)
    suspend fun updateWifiOnly(wifiOnly: Boolean)
    suspend fun updateNotifications(notifications: Boolean)

}