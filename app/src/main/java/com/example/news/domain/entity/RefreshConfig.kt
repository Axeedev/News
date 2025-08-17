package com.example.news.domain.entity

data class RefreshConfig(
    val language: Language,
    val period: Period,
    val wifiOnly: Boolean
)

fun Settings.toRefreshConfig(): RefreshConfig{
    return RefreshConfig(
        language = language,
        period = period,
        wifiOnly = wifiOnly
    )
}