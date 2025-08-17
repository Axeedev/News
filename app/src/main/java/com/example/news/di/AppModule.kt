package com.example.news.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import androidx.work.impl.WorkManagerImpl
import com.example.news.data.local.NewsDao
import com.example.news.data.local.NewsDatabase
import com.example.news.data.remote.ApiService
import com.example.news.data.repository.NewsRepositoryImpl
import com.example.news.data.repository.SettingsRepositoryImpl
import com.example.news.domain.repository.NewsRepository
import com.example.news.domain.repository.SettingsRepository
import com.example.news.domain.usecases.AddSubscriptionUseCase
import com.example.news.domain.usecases.CleanAllArticlesUseCase
import com.example.news.domain.usecases.GetAllSubscriptionsUseCase
import com.example.news.domain.usecases.GetArticlesByTopicsUseCase
import com.example.news.domain.usecases.RemoveSubscriptionUseCase
import com.example.news.domain.usecases.UpdateArticlesForTopicUseCase
import com.example.news.domain.usecases.UpdateArticlesUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface AppModule {


    @Singleton
    @Binds
    fun bindRepository(newsRepositoryImpl: NewsRepositoryImpl): NewsRepository

    @Singleton
    @Binds
    fun bindSettingsRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository
    companion object {

        @Singleton
        @Provides
        fun provideJson() : Json{
            return Json{
                ignoreUnknownKeys = true
                coerceInputValues = true
            }
        }
        @Singleton
        @Provides
        fun provideWorkManager(@ApplicationContext context: Context): WorkManager{
            return WorkManager.getInstance(context)
        }

        @Singleton
        @Provides
        fun provideConverterFactory(json: Json): Converter.Factory{
            return json.asConverterFactory("application/json".toMediaType())
        }

        @Singleton
        @Provides
        fun provideRetrofit(converterFactory: Converter.Factory): Retrofit{
            return Retrofit
                .Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(converterFactory)
                .build()
        }
        @Singleton
        @Provides
        fun provideApiService(retrofit: Retrofit): ApiService{
            return retrofit.create(ApiService::class.java)
        }

        @Singleton
        @Provides
        fun provideDatabase(@ApplicationContext context: Context): NewsDatabase {
            return Room.databaseBuilder(
                klass = NewsDatabase::class.java,
                name = "news database",
                context = context)
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
        }

        @Singleton
        @Provides
        fun provideDao(database: NewsDatabase): NewsDao{
            return database.newsDao()
        }

        @Singleton
        @Provides
        fun provideAddSubscriptionUseCase(repository: NewsRepository, settingsRepository: SettingsRepository): AddSubscriptionUseCase{
            return AddSubscriptionUseCase(repository,settingsRepository)
        }
        @Singleton
        @Provides
        fun provideCleanAllArticlesUseCase(repository: NewsRepository): CleanAllArticlesUseCase{
            return CleanAllArticlesUseCase(repository)
        }
        @Singleton
        @Provides
        fun provideGetAllSubscriptionsUseCase(repository: NewsRepository): GetAllSubscriptionsUseCase{
            return GetAllSubscriptionsUseCase(repository)
        }
        @Singleton
        @Provides
        fun provideGetArticlesByTopicsUseCase(repository: NewsRepository): GetArticlesByTopicsUseCase{
            return GetArticlesByTopicsUseCase(repository)
        }
        @Singleton
        @Provides
        fun provideRemoveSubscriptionUseCase(repository: NewsRepository): RemoveSubscriptionUseCase{
            return RemoveSubscriptionUseCase(repository)
        }
        @Singleton
        @Provides
        fun provideUpdateArticlesUseCase(repository: NewsRepository, settingsRepository: SettingsRepository): UpdateArticlesUseCase{
            return UpdateArticlesUseCase(repository, settingsRepository)
        }
        @Singleton
        @Provides
        fun provideUpdateArticlesForTopicUseCase(repository: NewsRepository, settingsRepository: SettingsRepository): UpdateArticlesForTopicUseCase{
            return UpdateArticlesForTopicUseCase(repository, settingsRepository)
        }

    }

}