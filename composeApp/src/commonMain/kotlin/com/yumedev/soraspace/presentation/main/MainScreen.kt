package com.yumedev.soraspace.presentation.main

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yumedev.soraspace.data.repository.AsteroidRepositoryImpl
import com.yumedev.soraspace.data.repository.EonetRepositoryImpl
import com.yumedev.soraspace.data.repository.MediaRepositoryImpl
import com.yumedev.soraspace.data.repository.LaunchRepositoryImpl
import com.yumedev.soraspace.data.repository.SpaceNewsRepositoryImpl
import com.yumedev.soraspace.data.repository.SpaceWeatherRepositoryImpl
import com.yumedev.soraspace.domain.model.NasaMedia
import com.yumedev.soraspace.presentation.favorites.FavoritesScreen
import com.yumedev.soraspace.presentation.home.HomeScreen
import com.yumedev.soraspace.presentation.home.HomeViewModel
import com.yumedev.soraspace.presentation.media_explorer.MediaExplorerViewModel
import com.yumedev.soraspace.presentation.media_explorer.MarsScreen
import com.yumedev.soraspace.presentation.navigation.FavoritesRoute
import com.yumedev.soraspace.presentation.navigation.HomeRoute
import com.yumedev.soraspace.presentation.navigation.MarsRoute
import com.yumedev.soraspace.presentation.navigation.SearchRoute
import com.yumedev.soraspace.presentation.navigation.SettingsRoute
import com.yumedev.soraspace.presentation.search.SearchViewModel
import com.yumedev.soraspace.presentation.search.SearchScreen
import com.yumedev.soraspace.presentation.settings.SettingsScreen
import com.yumedev.soraspace.ui.strings.LocalStrings
import com.yumedev.soraspace.ui.theme.SoraColors
import com.yumedev.soraspace.ui.theme.SoraType

private enum class Tab(val icon: ImageVector) {
    Home(Icons.Filled.Home),
    Explore(Icons.Filled.Explore),
    Search(Icons.Filled.Search),
    Favorites(Icons.Filled.Favorite),
    Settings(Icons.Filled.Settings)
}

@Composable
fun MainScreen(
    onNavigateToApod: () -> Unit,
    onNavigateToMediaDetail: (NasaMedia) -> Unit
) {
    val innerNav = rememberNavController()
    val backStackEntry by innerNav.currentBackStackEntryAsState()
    val currentDest = backStackEntry?.destination

    val selectedTab = when {
        currentDest?.hasRoute<MarsRoute>()      == true -> Tab.Explore
        currentDest?.hasRoute<SearchRoute>()    == true -> Tab.Search
        currentDest?.hasRoute<FavoritesRoute>() == true -> Tab.Favorites
        currentDest?.hasRoute<SettingsRoute>()  == true -> Tab.Settings
        else                                             -> Tab.Home
    }

    val s = LocalStrings.current

    Scaffold(
        containerColor = SoraColors.Background,
        bottomBar = {
            NavigationBar(
                containerColor = SoraColors.Surface,
                tonalElevation = 0.dp
            ) {
                Tab.entries.forEach { tab ->
                    val tabLabel = when (tab) {
                        Tab.Home      -> s.navHome
                        Tab.Explore   -> s.navExplore
                        Tab.Search    -> s.navSearch
                        Tab.Favorites -> s.navSaved
                        Tab.Settings  -> s.navSettings
                    }
                    NavigationBarItem(
                        selected = selectedTab == tab,
                        onClick  = {
                            val route: Any = when (tab) {
                                Tab.Home      -> HomeRoute
                                Tab.Explore   -> MarsRoute
                                Tab.Search    -> SearchRoute
                                Tab.Favorites -> FavoritesRoute
                                Tab.Settings  -> SettingsRoute
                            }
                            innerNav.navigate(route) {
                                popUpTo<HomeRoute> { saveState = true }
                                launchSingleTop = true
                                restoreState    = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector        = tab.icon,
                                contentDescription = tabLabel,
                                modifier           = Modifier.size(22.dp)
                            )
                        },
                        label  = { Text(text = tabLabel, style = SoraType.Caption) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor   = SoraColors.Accent,
                            selectedTextColor   = SoraColors.Accent,
                            indicatorColor      = SoraColors.AccentSubtle,
                            unselectedIconColor = SoraColors.TextSecondary,
                            unselectedTextColor = SoraColors.TextSecondary
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController    = innerNav,
            startDestination = HomeRoute,
            modifier         = Modifier.padding(innerPadding).consumeWindowInsets(innerPadding),
            enterTransition  = { fadeIn(animationSpec = tween(200)) },
            exitTransition   = { fadeOut(animationSpec = tween(150)) }
        ) {
            composable<HomeRoute> {
                val vm = viewModel { HomeViewModel(SpaceWeatherRepositoryImpl(), SpaceNewsRepositoryImpl(), LaunchRepositoryImpl()) }
                HomeScreen(
                    viewModel             = vm,
                    onNavigateToApod      = onNavigateToApod,
                    onNavigateToMars      = {
                        innerNav.navigate(MarsRoute) {
                            popUpTo<HomeRoute> { saveState = true }
                            launchSingleTop = true
                            restoreState    = true
                        }
                    },
                    onNavigateToSearch    = {
                        innerNav.navigate(SearchRoute) {
                            popUpTo<HomeRoute> { saveState = true }
                            launchSingleTop = true
                            restoreState    = true
                        }
                    },
                    onNavigateToFavorites = {
                        innerNav.navigate(FavoritesRoute) {
                            popUpTo<HomeRoute> { saveState = true }
                            launchSingleTop = true
                            restoreState    = true
                        }
                    }
                )
            }

            composable<MarsRoute> {
                val vm = viewModel { MediaExplorerViewModel(MediaRepositoryImpl()) }
                MarsScreen(
                    viewModel          = vm,
                    onBack             = { innerNav.popBackStack() },
                    onNavigateToDetail = onNavigateToMediaDetail
                )
            }

            composable<SearchRoute> {
                val vm = viewModel { SearchViewModel(AsteroidRepositoryImpl(), EonetRepositoryImpl()) }
                SearchScreen(viewModel = vm, onBack = { innerNav.popBackStack() })
            }

            composable<FavoritesRoute> {
                FavoritesScreen(onBack = { innerNav.popBackStack() })
            }

            composable<SettingsRoute> {
                SettingsScreen(onBack = { innerNav.popBackStack() })
            }
        }
    }
}
