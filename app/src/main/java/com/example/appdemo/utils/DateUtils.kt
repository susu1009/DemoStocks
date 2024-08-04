package com.example.appdemo.utils

import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
    val currentDate = Date()
    return dateFormat.format(currentDate)
}