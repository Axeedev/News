package com.example.news.domain.usecases

import com.example.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class GetAllSubscriptionsUseCase(private val repository: NewsRepository) {

    operator fun invoke(): Flow<List<String>> {
        return repository.getAllSubscriptions()
    }
}