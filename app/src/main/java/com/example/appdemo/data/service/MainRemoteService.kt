package com.example.appdemo.data.service

import com.example.appdemo.data.model.Stocks
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class MainRemoteService @Inject constructor() {

    private val listStocks by lazy {
        arrayListOf(
            Stocks(name = "VNM" , publisher = "VanEck Vectors VietNam ETF" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "Dow Jones" , publisher = "Dow Jones Industrial Average" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "AAPL" , publisher = "Apple Inc. " , currentValue = 13.03f , valueChange = 1.25f),
            Stocks(name = "SBUX" , publisher = "Starbucks Corporation" , currentValue = 13.03f , valueChange = 2.15f),
            Stocks(name = "NKE" , publisher = "Nike, Inc." , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "BHP" , publisher = "BHP Billiton Limited" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),
            Stocks(name = "THC" , publisher = "Tenet Healthcare Corporation" , currentValue = 13.03f , valueChange = -0.15f),

        )
    }

    @Volatile
    private var _isRetry = false
    val isRetry get() = _isRetry

    fun fakeData(): Flow<List<Stocks>> = flow {
        while (true) {
            if (_isRetry) {
                throw TimeoutException("Timeout")
            } else {
                val updatedStocks = listStocks.map { stock ->
                    val percentageChange = (-10..10).random() / 100f
                    val stockCurrentValue = stock.currentValue
                    val newValue = stockCurrentValue + percentageChange * stockCurrentValue
                    val roundedNewValue = String.format("%.2f", newValue).toFloat()
                    val changeValue = String.format("%.2f", roundedNewValue - stockCurrentValue).toFloat()
                    stock.copy(
                        currentValue = roundedNewValue,
                        valueChange = changeValue,
                        oldValue = stock.oldValue.apply {
                            removeFirstOrNull()
                            add(roundedNewValue)
                        }
                    )
                }
                emit(updatedStocks)
                delay(1000L)
            }
        }
    }.retry {
        delay(1000L)
        true
    }

    fun retryTest() {
        _isRetry = !_isRetry
    }

}