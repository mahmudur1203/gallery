package com.myapp.gallery.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myapp.gallery.screens.albums.AlbumsScreen
import com.myapp.gallery.screens.medialist.MediaListScreen


@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "albums") {
        composable("albums") {
            AlbumsScreen(navController = navController)
        }
        composable("media_list/{albumId}/{albumName}") { backStackEntry ->
            val albumId = backStackEntry.arguments?.getString("albumId")?.toLongOrNull() ?: -1L
            val albumName = backStackEntry.arguments?.getString("albumName") ?: "Media"
            MediaListScreen(albumId = albumId, albumName = albumName, navController = navController)
        }
    }
}