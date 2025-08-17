package com.example.news.domain.usecases

import com.example.news.domain.entity.toRefreshConfig
import com.example.news.domain.repository.NewsRepository
import com.example.news.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class StartRefreshDataUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val repository: NewsRepository
) {
    suspend operator fun invoke(){
        settingsRepository.getSettings()
            .map { it.toRefreshConfig() }
            .distinctUntilChanged()
            .onEach { repository.startBackGroundTask(it) }
            .collect()
    }
}