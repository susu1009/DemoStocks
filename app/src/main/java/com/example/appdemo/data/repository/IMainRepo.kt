package com.example.appdemo.data.repository

import com.example.appdemo.data.model.Stocks
import kotlinx.coroutines.flow.Flow

interface IMainRepo {
    fun getData(): Flow<List<Stocks>>
    fun retryTest()
    fun getRetryTest(): Boolean
}