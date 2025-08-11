package com.example.news.domain.usecases

import com.example.news.domain.repository.NewsRepository

class UpdateArticlesUseCase(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(){
        repository.updateArticlesForAllTopics()
    }
}