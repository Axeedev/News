package com.example.news.domain.repository

import com.example.news.domain.entity.Article
import com.example.news.domain.entity.RefreshConfig
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getAllSubscriptions(): Flow<List<String>>

    suspend fun addSubscription(topic: String)

    suspend fun updateArticlesForTopic(topic: String)

    suspend fun removeSubscription(topic: String)

    suspend fun updateArticlesForAllTopics()

    fun getArticlesByTopics(topics: List<String>): Flow<List<Article>>

    suspend fun clearAllArticles(topics: List<String>)

    suspend fun startBackGroundTask(refreshConfig: RefreshConfig)
}