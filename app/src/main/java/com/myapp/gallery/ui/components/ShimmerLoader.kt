package com.myapp.gallery.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.myapp.gallery.ui.util.TestTag
import com.valentinilk.shimmer.shimmer

@Composable
fun ShimmerLoader(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .testTag(TestTag.SHIMMER_LOADER)
            .shimmer()
    ) {
        content()
    }
}
