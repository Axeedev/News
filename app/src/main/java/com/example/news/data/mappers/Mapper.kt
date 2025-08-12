package com.example.news.data.mappers

import com.example.news.data.local.ArticleDbModel
import com.example.news.data.remote.ResponseDto
import com.example.news.domain.entity.Article


fun ResponseDto.toDbModels(topic: String):List<ArticleDbModel>{
    
    return articles.map {
        ArticleDbModel(
            title = it.title,
            description = it.description,
            imageUrl = it.urlToImage,
            url = it.url,
            publishedAt =it.publishedAt.toLong(),
            sourceName = it.source.name,
            topic = topic
        )
    }.distinct()

}

fun ArticleDbModel.toEntity(): Article{
    return Article(
        title = title,
        description = description,
        imageUrl = imageUrl,
        source = sourceName,
        publishedAt = publishedAt,
        url = url
    )
}