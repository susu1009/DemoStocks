package com.example.appdemo.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdemo.data.model.Stocks
import com.example.appdemo.data.repository.IMainRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepo: IMainRepo) : ViewModel() {

    private val data: StateFlow<List<Stocks>> =
        mainRepo.getData().flowOn(Dispatchers.IO).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    private val _isRetry = mutableStateOf(false)
    val isRetry: Boolean get() = _isRetry.value

    val filteredData: StateFlow<List<Stocks>> = searchQuery.combine(data) { query, data ->
        if (query.isEmpty()) {
            data
        } else {
            data.filter { it.name.contains(query, ignoreCase = true) }
        }
    }.flowOn(Dispatchers.IO).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun retryTest() {
        mainRepo.retryTest()
        _isRetry.value = mainRepo.getRetryTest()
    }
}