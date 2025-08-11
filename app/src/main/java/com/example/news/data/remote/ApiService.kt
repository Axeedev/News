package com.example.news.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("v2/everything?apiKey=8dbc5221249b46e39f02b96ced9d7cc6")
    suspend fun getArticles(
        @Query("q") topic: String
    ): ResponseDto
}