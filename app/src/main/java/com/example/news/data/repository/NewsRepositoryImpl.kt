package com.example.news.data.repository

import com.example.news.data.local.ArticleDbModel
import com.example.news.data.local.NewsDao
import com.example.news.data.local.SubscriptionDbModel
import com.example.news.data.mappers.toDbModels
import com.example.news.data.mappers.toEntity
import com.example.news.data.remote.ApiService
import com.example.news.domain.entity.Article
import com.example.news.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val newsDao: NewsDao
) : NewsRepository {
    override fun getAllSubscriptions(): Flow<List<String>> {
        return newsDao.getAllSubscriptions().map { list ->
            list.map {
                it.topic
            }
        }
    }

    override suspend fun addSubscription(topic: String) {
         newsDao.insertSubscription(SubscriptionDbModel(topic))
    }

    override suspend fun updateArticlesForTopic(topic: String) {
        val articles = loadArticles(topic)
        newsDao.addArticles(articles)
    }

    override suspend fun removeSubscription(topic: String) {
        newsDao.removeSubscription(SubscriptionDbModel(topic))
    }

    override suspend fun updateArticlesForAllTopics() {
        val subscriptions = newsDao.getAllSubscriptions().first()

        withContext(Dispatchers.IO){
            subscriptions.forEach {
                launch {
                    updateArticlesForTopic(it.topic)
                }
            }
        }


    }

    private suspend fun loadArticles(topic: String): List<ArticleDbModel>{
        return api.getArticles(topic).toDbModels(topic)
    }

    override fun getArticlesByTopics(topics: List<String>): Flow<List<Article>> {
        return newsDao.getAllArticlesByTopic(topics).map { list ->
            list.map {
                it.toEntity()
            }

        }
    }

    override suspend fun clearAllArticles(topics: List<String>) {
        getAllSubscriptions().first().let {
            newsDao.deleteArticlesByTopics(it)
        }
    }
}