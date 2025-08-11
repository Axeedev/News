package com.example.news.domain.entity

data class Article(
    val title: String,
    val description: String,
    val imageUrl: String?,
    val source: String,
    val publishedAt: Long,
    val url: String

)