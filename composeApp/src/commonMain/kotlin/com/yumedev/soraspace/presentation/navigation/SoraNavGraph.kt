package com.yumedev.soraspace.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.yumedev.soraspace.data.repository.ApodRepositoryImpl
import com.yumedev.soraspace.data.repository.MediaRepositoryImpl
import com.yumedev.soraspace.domain.model.NasaMedia
import com.yumedev.soraspace.presentation.apod.ApodScreen
import com.yumedev.soraspace.presentation.apod.ApodViewModel
import com.yumedev.soraspace.presentation.main.MainScreen
import com.yumedev.soraspace.presentation.media_explorer.MediaDetailScreen
import com.yumedev.soraspace.presentation.media_explorer.MediaDetailViewModel
import kotlinx.serialization.Serializable

@Serializable object MainRoute
@Serializable object HomeRoute
@Serializable object ApodRoute
@Serializable object MarsRoute
@Serializable object SearchRoute
@Serializable object FavoritesRoute
@Serializable object SettingsRoute

@Serializable
data class MediaDetailRoute(
    val nasaId: String,
    val title: String,
    val description: String? = null,
    val thumbnailUrl: String? = null,
    val mediaType: String,
    val dateCreated: String? = null,
    val center: String? = null
)

@Composable
fun SoraNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController    = navController,
        startDestination = MainRoute,
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
        composable<MainRoute> {
            MainScreen(
                onNavigateToApod        = { navController.navigate(ApodRoute) },
                onNavigateToMediaDetail = { media ->
                    navController.navigate(
                        MediaDetailRoute(
                            nasaId       = media.id,
                            title        = media.title,
                            description  = media.description,
                            thumbnailUrl = media.thumbnailUrl,
                            mediaType    = media.mediaType,
                            dateCreated  = media.dateCreated,
                            center       = media.center
                        )
                    )
                }
            )
        }

        composable<ApodRoute> {
            val vm = viewModel { ApodViewModel(ApodRepositoryImpl()) }
            ApodScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }

        composable<MediaDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<MediaDetailRoute>()
            val media = NasaMedia(
                id           = route.nasaId,
                title        = route.title,
                description  = route.description,
                thumbnailUrl = route.thumbnailUrl,
                mediaType    = route.mediaType,
                dateCreated  = route.dateCreated,
                center       = route.center
            )
            val vm = viewModel {
                MediaDetailViewModel(MediaRepositoryImpl(), route.nasaId, media.isVideo)
            }
            MediaDetailScreen(
                media     = media,
                viewModel = vm,
                onBack    = { navController.popBackStack() }
            )
        }
    }
}