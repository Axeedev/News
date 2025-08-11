package com.example.news.di

import android.content.Context
import androidx.room.Room
import com.example.news.data.local.NewsDao
import com.example.news.data.local.NewsDatabase
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

    }

}