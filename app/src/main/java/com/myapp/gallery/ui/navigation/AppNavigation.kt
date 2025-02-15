package com.myapp.gallery.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myapp.gallery.screens.albums.AlbumsScreen
import com.myapp.gallery.screens.medialist.MediaListScreen


@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "albums") {

        animatedComposable("albums",
            content = {
                AlbumsScreen(navController = navController)
            }
        )

        animatedComposable("media_list/{albumId}/{albumName}",
            content = { backStackEntry ->
                val albumId = backStackEntry.arguments?.getString("albumId")?.toLongOrNull() ?: -1L
                val albumName = backStackEntry.arguments?.getString("albumName") ?: "Media"
                MediaListScreen(
                    albumId = albumId,
                    albumName = albumName,
                    navController = navController
                )
            }
        )

    }
}


fun NavGraphBuilder.animatedComposable(
    route: String,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
        content = content

    )
}
