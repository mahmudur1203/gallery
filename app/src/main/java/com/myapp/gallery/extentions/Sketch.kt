package com.myapp.gallery.extentions

import com.github.panpf.sketch.SingletonSketch
import com.github.panpf.sketch.Sketch
import com.github.panpf.sketch.cache.MemoryCache
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.sketch.resize.Precision
import com.github.panpf.sketch.resize.Scale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun Sketch.preload(uri: String) {
    val request = ImageRequest(context, uri) {
        size(20, 20)
        precision(Precision.LESS_PIXELS)
        scale(Scale.CENTER_CROP)
    }

    enqueue(request)

    CoroutineScope(Dispatchers.IO).launch {
        execute(request)
    }
}
