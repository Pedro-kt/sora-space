package com.yumedev.soraspace.presentation.apod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.soraspace.domain.repository.ApodRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ApodViewModel(
    private val repository: ApodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ApodUiState>(ApodUiState.Loading)
    val uiState: StateFlow<ApodUiState> = _uiState.asStateFlow()

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {
            _uiState.value = ApodUiState.Loading
            _uiState.value = try {
                supervisorScope {
                    val featuredDeferred = async { repository.getApodOfTheDay() }
                    val feedDeferred     = async { repository.getRecentApods() }
                    ApodUiState.Success(
                        featured = featuredDeferred.await(),
                        feed     = feedDeferred.await()
                    )
                }
            } catch (e: Exception) {
                ApodUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun toggleFavorite(date: String) {
        _favorites.update { current ->
            if (date in current) current - date else current + date
        }
    }
}