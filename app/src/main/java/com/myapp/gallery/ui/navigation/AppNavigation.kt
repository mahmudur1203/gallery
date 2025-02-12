package com.myapp.gallery.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.myapp.gallery.ui.albums.GalleryScreen


@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "gallery") {
        composable("gallery") {
            GalleryScreen()
        }
    }
}