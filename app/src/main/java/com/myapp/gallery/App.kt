package com.myapp.gallery

import android.app.Application
import coil.util.DebugLogger
import com.github.panpf.sketch.SingletonSketch
import com.github.panpf.sketch.Sketch
import com.github.panpf.sketch.cache.MemoryCache
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Todo:: Will check Sketch to for better image rendering
        SingletonSketch.Factory { context ->
            Sketch.Builder(context)
                .apply {
                    memoryCache(
                        MemoryCache.Builder(context)
                            .maxSizePercent(0.5)
                            .build()
                    )
                }
                .build()
        }.apply { SingletonSketch.setSafe(this) }
    }
}