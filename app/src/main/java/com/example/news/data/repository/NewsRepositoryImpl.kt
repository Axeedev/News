package com.example.news.data.repository

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.news.data.backgound.RefreshWorker
import com.example.news.data.local.ArticleDbModel
import com.example.news.data.local.NewsDao
import com.example.news.data.local.SubscriptionDbModel
import com.example.news.data.mappers.toDbModels
import com.example.news.data.mappers.toEntity
import com.example.news.data.remote.ApiService
import com.example.news.domain.entity.Article
import com.example.news.domain.entity.Language
import com.example.news.domain.entity.RefreshConfig
import com.example.news.domain.entity.toQueryParam
import com.example.news.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val newsDao: NewsDao,
    private val workManager: WorkManager
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

    override suspend fun updateArticlesForTopic(topic: String, language: Language) : Boolean {
        val articles = loadArticles(topic, language)
        val list = newsDao.addArticles(articles)
        return list.any { it != 1L }
    }

    override suspend fun removeSubscription(topic: String) {
        newsDao.removeSubscription(SubscriptionDbModel(topic))
    }
    override suspend fun startBackGroundTask(refreshConfig: RefreshConfig){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(
                if (refreshConfig.wifiOnly) NetworkType.UNMETERED
                else NetworkType.CONNECTED
            )
            .setRequiresBatteryNotLow(true)
            .build()
        val request =
            PeriodicWorkRequestBuilder<RefreshWorker>(refreshConfig.period.minutes.toLong(),
                TimeUnit.MINUTES
            ).setConstraints(constraints).build()

        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName = "refresh data",
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            request = request
        )
    }

    override suspend fun updateArticlesForAllTopics(language: Language): List<String> {
        val subscriptions = newsDao.getAllSubscriptions().first()

        val newTopics = mutableListOf<String>()

        withContext(Dispatchers.IO){
            subscriptions.forEach {
                launch {
                    val hasNew = updateArticlesForTopic(it.topic, language)
                    if (hasNew) newTopics.add(it.topic)
                }
            }
        }

        return newTopics

    }

    private suspend fun loadArticles(topic: String, language: Language): List<ArticleDbModel>{
        return api.getArticles(topic, language.toQueryParam()).toDbModels(topic)
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