package com.yumedev.soraspace.presentation.apod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.soraspace.domain.repository.ApodRepository
import com.yumedev.soraspace.domain.repository.FavoritesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class ApodViewModel(
    private val repository: ApodRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ApodUiState>(ApodUiState.Loading)
    val uiState: StateFlow<ApodUiState> = _uiState.asStateFlow()

    val favorites: StateFlow<Set<String>> = favoritesRepository.observeApodFavorites()
        .map { list -> list.map { it.date }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

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
        viewModelScope.launch {
            val state = _uiState.value as? ApodUiState.Success ?: return@launch
            val apod = if (state.featured.date == date) state.featured
                       else state.feed.find { it.date == date } ?: return@launch
            favoritesRepository.toggleApodFavorite(apod)
        }
    }
}