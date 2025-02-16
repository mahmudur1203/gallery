package com.myapp.gallery.screens.medialist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.SingletonSketch
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.sketch.state.ThumbnailMemoryCacheStateImage
import com.myapp.gallery.domain.model.Media
import com.myapp.gallery.domain.state.Resource
import com.myapp.gallery.ui.components.ErrorMessage
import com.myapp.gallery.ui.components.ShimmerLoader
import com.myapp.gallery.ui.util.ContentDescriptions
import com.myapp.gallery.ui.util.TestTag
import com.myapp.gallery.util.Utils


@Composable
fun MediaListScreen(
    viewModel: MediaListViewModel = hiltViewModel(),
    albumId: Long,
    albumName: String,
    navController: NavHostController
) {

    LaunchedEffect(albumId) {
        viewModel.fetchMedias(albumId)
    }

    val mediaResource by viewModel.medias.collectAsState()
    var selectedMedia by remember { mutableStateOf<Media?>(null) }

    Scaffold(
        topBar = {
            TopBar(title = albumName, onBackClick = { navController.popBackStack() })
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {

                MediaListScreenContent(mediaResource,
                    onClickItem = {
                        selectedMedia = it
                    },
                    onRetryClick = {
                        viewModel.fetchMedias(albumId)
                    }
                )

                // Show image/video on selection
                selectedMedia?.let { media ->

                    if(mediaResource is Resource.Success<List<Media>> ){
                        val mediaList = (mediaResource as Resource.Success<List<Media>>).data
                        FullScreenMediaViewer(mediaList,
                            mediaList.indexOf(media)) {
                            selectedMedia = null
                        }
                    }

                }
            }
        }
    )

}

@Composable
fun MediaListScreenContent(
    mediasResource: Resource<List<Media>>,
    onClickItem: (Media) -> Unit,
    onRetryClick: () -> Unit
) {
    when (mediasResource) {
        Resource.Loading -> {

            //Show shimmer effect while loading
            ShimmerGridLoader(modifier = Modifier.testTag(TestTag.LOADING_INDICATOR))
        }

        is Resource.Error -> {

            // Show error message with retry button
            ErrorMessage(mediasResource.message, onRetryClick = onRetryClick)
        }

        is Resource.Success -> {
            // Display media list in Grid
            MediaList(mediasResource.data, onClickItem = onClickItem)
        }

        Resource.Empty -> {}
    }
}

@Composable
fun MediaList(medias: List<Media>, onClickItem: (Media) -> Unit) {


    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 80.dp),
            modifier = Modifier
                .fillMaxSize()
                .testTag(TestTag.MEDIA_LIST),
            contentPadding = PaddingValues(horizontal = 1.dp, vertical = 1.dp)

        ) {
            items(medias, key = { it.id }) { media ->
                MediaItem(media, onClickItem)
//                LaunchedEffect(media.uri) {
//                    sketch.preload(uri = media.uri)
//                }
            }
        }

    }

}


@Composable
fun MediaItem(media: Media, onClickItem: (Media) -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(0.75.dp)
            .testTag( TestTag.MEDIA_ITEM_PREFIX + media.id)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh
            )
           // .clipToBounds()

    ) {

        ImageItem(media.uri, onClickItem = {
            onClickItem(media)
        })

        if (media.isVideo) {
            Text(
                text = Utils.formatDuration(media.duration ?: 0L),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(2.dp),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )
        }

    }

}

@Composable
fun ImageItem(uri: String, onClickItem: () -> Unit) {

//    val request = ImageRequest(LocalContext.current, uri)
//    {
//        placeholder(ThumbnailMemoryCacheStateImage(uri))
//        crossfade(fadeStart = false)
//    }

    val context = LocalContext.current
    val sketch = remember { SingletonSketch.get(context) }

    AsyncImage(
        //request = request,
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                onClickItem()
            },
        uri = uri.toString(),
        contentDescription = ContentDescriptions.MEDIA_IMAGE,
        contentScale = ContentScale.Crop,
        sketch = sketch,
        filterQuality = FilterQuality.None,
        clipToBounds = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, onBackClick: () -> Unit) {

    TopAppBar(
        title = {
            Text(
                modifier = Modifier.testTag(TestTag.TITLE),
                text = title
            )
        },

        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = ContentDescriptions.BACK_BUTTON
                )
            }
        },
    )

}

@Composable
fun ShimmerGridLoader(modifier: Modifier = Modifier) {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 80.dp),
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 1.dp, vertical = 1.dp)
    ) {

        items(20) {
            ShimmerLoader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()

                        .aspectRatio(1f)
                        .padding(0.75.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                )
            }
        }
    }
}




