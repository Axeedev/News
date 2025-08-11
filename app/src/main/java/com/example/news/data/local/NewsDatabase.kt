package com.example.news.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ArticleDbModel::class, SubscriptionDbModel::class],
    exportSchema = false,
    version = 1
)
abstract class NewsDatabase : RoomDatabase(){

    abstract fun newsDao(): NewsDao

}