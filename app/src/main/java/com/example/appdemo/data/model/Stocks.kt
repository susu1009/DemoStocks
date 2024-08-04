package com.example.appdemo.data.model

data class Stocks(
    val name: String,
    val publisher: String,
    val currentValue: Float,
    val valueChange: Float,
    val oldValue: ArrayList<Float> = arrayListOf(
        -2f,
        -1f,
        0f,
        1f,
        -1f,
        -2f,
        5f,
        8f,
        -9f,
        10f,
        9f,
        8f,
        7f,
        6f,
        5f,
        4f,
        3f,
        2f,
        1f
    )
)