package com.myapp.gallery.screens

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.myapp.gallery.ui.navigation.AppNavigation
import com.myapp.gallery.ui.theme.GalleryTheme
import com.myapp.gallery.util.PERMISSIONS
import com.myapp.gallery.util.PermissionState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        installSplashScreen()

        super.onCreate(savedInstanceState)



        enableEdgeToEdge()

        setContent {

            GalleryTheme {

                var permissionState by rememberSaveable { mutableStateOf(PermissionState.NONE) }

                val context = LocalContext.current

                val permissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    val allPermissionsGranted = permissions.values.all { it }
                    permissionState = if (allPermissionsGranted) {
                        PermissionState.GRANTED
                    } else {
                        PermissionState.DENIED
                    }
                }

                LaunchedEffect(Unit) {
                    val allPermissionsGranted = PERMISSIONS.all { permission ->
                        ContextCompat.checkSelfPermission(
                            context,
                            permission
                        ) == PackageManager.PERMISSION_GRANTED
                    }

                    if (allPermissionsGranted) {
                        permissionState = PermissionState.GRANTED
                    } else {
                        permissionLauncher.launch(PERMISSIONS.toTypedArray())
                    }
                }

                if (permissionState == PermissionState.DENIED) {
                    Surface(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        PermissionsScreen(onPermissionGranted = {
                            permissionState = PermissionState.GRANTED
                        })
                    }
                } else if (permissionState == PermissionState.GRANTED) {

                    AppNavigation()

                }

            }
        }
    }
}

