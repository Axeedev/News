package com.example.news.domain.usecases

import com.example.news.domain.repository.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class AddSubscriptionUseCase(private val repository: NewsRepository){

    suspend operator fun invoke(topic: String){
        repository.addSubscription(topic)
        CoroutineScope(coroutineContext).launch {
            repository.updateArticlesForTopic(topic)
        }
    }
}