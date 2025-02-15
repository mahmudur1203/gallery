package com.myapp.gallery.screens.medialist

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.panpf.sketch.SingletonSketch
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.sketch.state.ThumbnailMemoryCacheStateImage
import com.myapp.gallery.domain.model.Media
import com.myapp.gallery.ui.util.ContentDescriptions


@Composable
fun FullScreenMediaViewer(
    mediaList: List<Media>,
    initialPage: Int,
    onDismiss: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {

        val pagerState = rememberPagerState(
            initialPage = initialPage,
            initialPageOffsetFraction = 0f,
            pageCount = mediaList::size
        )

        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    val media = mediaList[page]

                    if (media.isVideo) {
                        AndroidView(
                            factory = { context ->
                                VideoView(context).apply {
                                    setVideoURI(Uri.parse(media.uri))
                                    start()
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {

                        val context = LocalContext.current
                        val sketch = remember { SingletonSketch.get(context) }

                        val request = ImageRequest(LocalContext.current, media.uri)
                        {
                            placeholder(ThumbnailMemoryCacheStateImage(media.uri))
                            crossfade(fadeStart = false)
                        }

                        com.github.panpf.sketch.AsyncImage(
                            request = request,
                            modifier = Modifier
                                .fillMaxSize(),
                            //uri = uri.toString(),
                            contentDescription = ContentDescriptions.MEDIA_IMAGE,
                            contentScale = ContentScale.Fit,
                            sketch = sketch
                        )

                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = ContentDescriptions.CLOSE,
                        tint = Color.White
                    )
                }
            }
        }


    }


}

