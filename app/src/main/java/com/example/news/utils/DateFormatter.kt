package com.example.news.utils

import android.icu.text.DateFormat
import java.text.SimpleDateFormat
import java.util.logging.SimpleFormatter

object DateFormatter {
    private val formatter = SimpleDateFormat.getDateInstance(DateFormat.SHORT)
    fun Long.toDateFormat(): String{
        return formatter.format(this)
    }

}
