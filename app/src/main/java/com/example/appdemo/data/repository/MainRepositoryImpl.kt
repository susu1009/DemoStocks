package com.example.appdemo.data.repository

import com.example.appdemo.data.model.Stocks
import com.example.appdemo.data.service.MainRemoteService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val mainRemoteService: MainRemoteService):
    IMainRepo {
    override fun getData(): Flow<List<Stocks>> = mainRemoteService.fakeData()
    override fun retryTest() {
        mainRemoteService.retryTest()
    }

    override fun getRetryTest(): Boolean = mainRemoteService.isRetry
}