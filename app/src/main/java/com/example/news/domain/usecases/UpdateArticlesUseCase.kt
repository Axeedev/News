package com.example.news.domain.usecases

import com.example.news.domain.repository.NewsRepository
import com.example.news.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first

class UpdateArticlesUseCase(
    private val repository: NewsRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): List<String> {
        val settings = settingsRepository.getSettings().first()
        return repository.updateArticlesForAllTopics(settings.language)
    }
}