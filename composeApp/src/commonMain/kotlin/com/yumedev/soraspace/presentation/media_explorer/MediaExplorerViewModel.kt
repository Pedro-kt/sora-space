package com.yumedev.soraspace.presentation.media_explorer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.soraspace.domain.model.NasaMedia
import com.yumedev.soraspace.domain.repository.FavoritesRepository
import com.yumedev.soraspace.domain.repository.MediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MediaExplorerViewModel(
    private val repository: MediaRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MediaExplorerUiState>(MediaExplorerUiState.Idle)
    val uiState: StateFlow<MediaExplorerUiState> = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val favoriteMediaIds: StateFlow<Set<String>> = favoritesRepository.observeMediaFavorites()
        .map { list -> list.map { it.id }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    fun onQueryChange(value: String) { _query.value = value }

    fun search(query: String = _query.value) {
        if (query.isBlank()) return
        _query.value = query
        viewModelScope.launch {
            _uiState.value = MediaExplorerUiState.Loading
            _uiState.value = try {
                val items = repository.search(query)
                if (items.isEmpty()) MediaExplorerUiState.Error("No results for \"$query\"")
                else MediaExplorerUiState.Success(items)
            } catch (e: Exception) {
                MediaExplorerUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun toggleMediaFavorite(media: NasaMedia) {
        viewModelScope.launch { favoritesRepository.toggleMediaFavorite(media) }
    }
}