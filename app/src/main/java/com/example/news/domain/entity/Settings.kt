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

enum class Language(val asString : String){
    RUSSIAN("Russian"), ENGLISH("English"), GERMAN("German"), FRENCH("French")
}

fun Language.toQueryParam(): String{
    return when(this){
        Language.RUSSIAN -> "ru"
        Language.ENGLISH -> "en"
        Language.GERMAN -> "de"
        Language.FRENCH -> "fr"
    }
}

enum class Period(val minutes: Int){

    MINUTES_15(15), MINUTES_30(30),
    HOURS_1(60), HOURS_2(120),
    HOURS_4(240), HOURS_8(480), HOURS_24(1440)
}
fun Period.asString(): String{
    return when(this) {
        Period.MINUTES_15 -> {
            "15 minutes"
        }
        Period.MINUTES_30 -> {
            "30 minutes"
        }
        Period.HOURS_1 -> {
            "1 hour"
        }
        Period.HOURS_2 -> {
            "2 hours"
        }
        Period.HOURS_4 -> {
            "4 hours"
        }
        Period.HOURS_8 -> {
            "8 hours"
        }
        Period.HOURS_24 -> {
            "24 hours"
        }
    }
}
fun Int.toPeriod(): Period{
    return Period.entries.first { it.minutes == this }
}