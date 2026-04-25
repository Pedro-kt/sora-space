package com.yumedev.soraspace.presentation.media_explorer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.soraspace.domain.repository.MediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MediaDetailViewModel(
    private val repository: MediaRepository,
    private val nasaId: String,
    private val isVideo: Boolean
) : ViewModel() {

    private val _uiState = MutableStateFlow<MediaDetailUiState>(MediaDetailUiState.Loading)
    val uiState: StateFlow<MediaDetailUiState> = _uiState.asStateFlow()

    init { loadAsset() }

    fun retry() = loadAsset()

    private fun loadAsset() {
        viewModelScope.launch {
            _uiState.value = MediaDetailUiState.Loading
            _uiState.value = try {
                val url = repository.getAssetUrl(nasaId, isVideo)
                if (url != null) MediaDetailUiState.Success(url)
                else MediaDetailUiState.Error("Asset not available")
            } catch (e: Exception) {
                MediaDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}