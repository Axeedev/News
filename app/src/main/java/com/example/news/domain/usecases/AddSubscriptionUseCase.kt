package com.example.news.domain.usecases

import com.example.news.domain.repository.NewsRepository

class AddSubscriptionUseCase(private val repository: NewsRepository){

    suspend operator fun invoke(topic: String){
        repository.addSubscription(topic)
        repository.updateArticlesForTopic(topic)
    }
}