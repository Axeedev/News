package com.example.news.data.mappers

import com.example.news.data.local.ArticleDbModel
import com.example.news.data.remote.ResponseDto
import com.example.news.domain.entity.Article
import java.text.SimpleDateFormat
import java.util.Locale


fun ResponseDto.toDbModels(topic: String):List<ArticleDbModel>{
    
    return articles.map {
        ArticleDbModel(
            title = it.title,
            description = it.description,
            imageUrl = it.urlToImage,
            url = it.url,
            publishedAt =it.publishedAt.toTimeStamp(),
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


fun String.toTimeStamp(): Long{
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    return dateFormatter.parse(this)?.time ?: System.currentTimeMillis()
}