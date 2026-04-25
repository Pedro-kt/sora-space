package com.yumedev.soraspace.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yumedev.soraspace.data.repository.ApodRepositoryImpl
import com.yumedev.soraspace.data.repository.MediaRepositoryImpl
import com.yumedev.soraspace.presentation.apod.ApodScreen
import com.yumedev.soraspace.presentation.apod.ApodViewModel
import com.yumedev.soraspace.presentation.media_explorer.MediaExplorerViewModel
import com.yumedev.soraspace.presentation.favorites.FavoritesScreen
import com.yumedev.soraspace.presentation.home.HomeScreen
import com.yumedev.soraspace.presentation.media_explorer.MarsScreen
import com.yumedev.soraspace.presentation.search.SearchScreen
import kotlinx.serialization.Serializable

// Rutas tipadas — el compilador garantiza que existan todas las pantallas
@Serializable object HomeRoute
@Serializable object ApodRoute
@Serializable object MarsRoute
@Serializable object SearchRoute
@Serializable object FavoritesRoute

@Composable
fun SoraNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController    = navController,
        startDestination = HomeRoute,
        enterTransition  = {
            slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) +
            fadeIn(animationSpec = tween(300))
        },
        exitTransition   = {
            slideOutHorizontally(targetOffsetX = { -it / 3 }, animationSpec = tween(300)) +
            fadeOut(animationSpec = tween(200))
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -it / 3 }, animationSpec = tween(300)) +
            fadeIn(animationSpec = tween(300))
        },
        popExitTransition  = {
            slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) +
            fadeOut(animationSpec = tween(200))
        }
    ) {
        composable<HomeRoute> {
            HomeScreen(
                onNavigateToApod      = { navController.navigate(ApodRoute) },
                onNavigateToMars      = { navController.navigate(MarsRoute) },
                onNavigateToSearch    = { navController.navigate(SearchRoute) },
                onNavigateToFavorites = { navController.navigate(FavoritesRoute) }
            )
        }

        composable<ApodRoute> {
            val vm = viewModel { ApodViewModel(ApodRepositoryImpl()) }
            ApodScreen(
                viewModel = vm,
                onBack    = { navController.popBackStack() }
            )
        }

        composable<MarsRoute> {
            val vm = viewModel { MediaExplorerViewModel(MediaRepositoryImpl()) }
            MarsScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }

        composable<SearchRoute> {
            SearchScreen(onBack = { navController.popBackStack() })
        }

        composable<FavoritesRoute> {
            FavoritesScreen(onBack = { navController.popBackStack() })
        }
    }
}