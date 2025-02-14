package com.myapp.gallery.util

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.Q)
private val PERMISSION_Q = listOf(
//    Manifest.permission.ACCESS_MEDIA_LOCATION,
    Manifest.permission.READ_EXTERNAL_STORAGE,
)

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private val PERMISSION_T = listOf(
    Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO
    //Manifest.permission.ACCESS_MEDIA_LOCATION
)


private val PERMISSION_OLD = listOf(Manifest.permission.READ_EXTERNAL_STORAGE)


val PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    PERMISSION_T
} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    PERMISSION_Q
} else {
    PERMISSION_OLD
}