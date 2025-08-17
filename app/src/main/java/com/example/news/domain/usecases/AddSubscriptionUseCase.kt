package com.example.news.domain.usecases

import com.example.news.domain.repository.NewsRepository
import com.example.news.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class AddSubscriptionUseCase(
    private val repository: NewsRepository,
    private val settingsRepository: SettingsRepository
){

    suspend operator fun invoke(topic: String){
        val settings = settingsRepository.getSettings().first()
        repository.addSubscription(topic)
        CoroutineScope(coroutineContext).launch {
            repository.updateArticlesForTopic(topic, language = settings.language)
        }
    }
}