package com.myapp.gallery.extentions

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build


fun Context.permissionGranted(list: List<String>): Boolean {
    var granted = true
    list.forEach {
        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
            } else {
                return true
            }
        ) granted = false
    }
    return granted
}

val Context.connectivityManager: ConnectivityManager
    get() =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager