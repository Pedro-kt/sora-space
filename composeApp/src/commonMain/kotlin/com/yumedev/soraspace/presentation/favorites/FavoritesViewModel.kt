package com.yumedev.soraspace.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.soraspace.domain.model.Apod
import com.yumedev.soraspace.domain.model.NasaMedia
import com.yumedev.soraspace.domain.model.SpaceArticle
import com.yumedev.soraspace.domain.model.SpaceLaunch
import com.yumedev.soraspace.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class FavoriteTab { All, Apod, Media, Launches, News }

class FavoritesViewModel(
    private val repository: FavoritesRepository
) : ViewModel() {

    val selectedTab = MutableStateFlow(FavoriteTab.All)

    val apodFavorites: StateFlow<List<Apod>> = repository.observeApodFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val mediaFavorites: StateFlow<List<NasaMedia>> = repository.observeMediaFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val launchFavorites: StateFlow<List<SpaceLaunch>> = repository.observeLaunchFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val articleFavorites: StateFlow<List<SpaceArticle>> = repository.observeArticleFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onTabSelected(tab: FavoriteTab) { selectedTab.value = tab }

    fun removeApod(apod: Apod) = viewModelScope.launch { repository.toggleApodFavorite(apod) }
    fun removeMedia(media: NasaMedia) = viewModelScope.launch { repository.toggleMediaFavorite(media) }
    fun removeLaunch(launch: SpaceLaunch) = viewModelScope.launch { repository.toggleLaunchFavorite(launch) }
    fun removeArticle(article: SpaceArticle) = viewModelScope.launch { repository.toggleArticleFavorite(article) }
}