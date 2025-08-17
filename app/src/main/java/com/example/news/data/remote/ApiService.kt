package com.example.news.data.remote

import com.example.news.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v2/everything?apiKey=${BuildConfig.API_KEY}")
    suspend fun getArticles(
        @Query("q") topic: String,
        @Query("language") language: String
    ): ResponseDto
}