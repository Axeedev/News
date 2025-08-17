package com.example.news.domain.usecases

import com.example.news.domain.repository.NewsRepository
import com.example.news.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first

class UpdateArticlesForTopicUseCase(
    private val repository: NewsRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(topic: String){
        val settings = settingsRepository.getSettings().first()
        repository.updateArticlesForTopic(topic, settings.language)
    }

}