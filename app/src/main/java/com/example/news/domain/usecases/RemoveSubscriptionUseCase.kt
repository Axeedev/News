package com.example.news.domain.usecases

import com.example.news.domain.repository.NewsRepository

class RemoveSubscriptionUseCase(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(topic: String){
        repository.removeSubscription(topic)
    }

}