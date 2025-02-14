package com.myapp.gallery.util

import android.annotation.SuppressLint
import java.util.Locale

object Utils {

    fun formatDuration(milliseconds: Long?): String {
        if (milliseconds == null) {
            return ""
        }
        val minutes = (milliseconds / 1000) / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format(Locale.US,"%02d:%02d", minutes, seconds)
    }

}