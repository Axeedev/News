package com.example.news.domain.usecases

import com.example.news.domain.repository.NewsRepository

class CleanAllArticlesUseCase(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(topics: List<String>){
        repository.clearAllArticles(topics)
    }
}