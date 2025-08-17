package com.example.news.domain.entity

data class Settings(
    val language: Language,
    val period: Period,
    val showNotifications: Boolean,
    val wifiOnly: Boolean
){
    companion object{
        val defaultEnglish = Language.ENGLISH
        val defaultPeriod = Period.MINUTES_15
        const val DEFAULT_WIFI_ONLY = false
        const val DEFAULT_NOTIFICATIONS = false
    }
}

enum class Language{
    RUSSIAN, ENGLISH, GERMAN, FRENCH
}

enum class Period(val minutes: Int){

    MINUTES_15(15), MINUTES_30(30),
    HOURS_1(60), HOURS_2(120),
    HOURS_4(240), HOURS_8(480), HOURS_24(1440)
}

fun Int.toPeriod(): Period{
    return Period.entries.first { it.minutes == this }
}