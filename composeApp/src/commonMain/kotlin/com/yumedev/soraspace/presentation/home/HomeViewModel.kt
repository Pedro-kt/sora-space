package com.yumedev.soraspace.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.soraspace.domain.model.SpaceArticle
import com.yumedev.soraspace.domain.model.SpaceLaunch
import com.yumedev.soraspace.domain.repository.FavoritesRepository
import com.yumedev.soraspace.domain.repository.LaunchRepository
import com.yumedev.soraspace.domain.repository.SpaceNewsRepository
import com.yumedev.soraspace.domain.repository.SpaceWeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val spaceWeatherRepository: SpaceWeatherRepository,
    private val spaceNewsRepository: SpaceNewsRepository,
    private val launchRepository: LaunchRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val favoriteArticleIds: StateFlow<Set<Int>> = favoritesRepository.observeArticleFavorites()
        .map { list -> list.map { it.id }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    val favoriteLaunchIds: StateFlow<Set<String>> = favoritesRepository.observeLaunchFavorites()
        .map { list -> list.map { it.id }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    init {
        loadSpaceWeather()
        loadNews()
        loadLaunches()
    }

    fun loadSpaceWeather() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, hasError = false) }
            try {
                val weather = spaceWeatherRepository.getSpaceWeather()
                _uiState.update { it.copy(spaceWeather = weather, isLoading = false) }
            } catch (_: Exception) {
                _uiState.update { it.copy(isLoading = false, hasError = true) }
            }
        }
    }

    fun loadNews() {
        viewModelScope.launch {
            _uiState.update { it.copy(isNewsLoading = true, hasNewsError = false) }
            try {
                val articles = spaceNewsRepository.getLatestArticles(limit = 6)
                _uiState.update {
                    it.copy(
                        featuredArticle = articles.firstOrNull(),
                        latestNews      = articles.drop(1).take(5),
                        isNewsLoading   = false
                    )
                }
            } catch (_: Exception) {
                _uiState.update { it.copy(isNewsLoading = false, hasNewsError = true) }
            }
        }
    }

    fun loadLaunches() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLaunchesLoading = true, hasLaunchesError = false) }
            try {
                val launches = launchRepository.getUpcomingLaunches(limit = 6)
                _uiState.update { it.copy(launches = launches, isLaunchesLoading = false) }
            } catch (_: Exception) {
                _uiState.update { it.copy(isLaunchesLoading = false, hasLaunchesError = true) }
            }
        }
    }

    fun toggleArticleFavorite(article: SpaceArticle) {
        viewModelScope.launch { favoritesRepository.toggleArticleFavorite(article) }
    }

    fun toggleLaunchFavorite(launch: SpaceLaunch) {
        viewModelScope.launch { favoritesRepository.toggleLaunchFavorite(launch) }
    }
}