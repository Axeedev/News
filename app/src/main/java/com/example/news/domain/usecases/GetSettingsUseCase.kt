package com.example.news.domain.usecases

import com.example.news.domain.entity.Settings
import com.example.news.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Settings> {
        return repository.getSettings()
    }
}