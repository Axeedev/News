package com.example.news.data.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDto(
    @SerialName("articles")
    val articles: List<ArticleDto> = listOf(),
)