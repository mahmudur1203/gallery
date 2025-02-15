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
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.LocalPlatformContext
import com.github.panpf.sketch.SingletonSketch
import com.myapp.gallery.domain.model.Media
import com.myapp.gallery.domain.state.Resource
import com.myapp.gallery.screens.albums.ErrorMessage
import com.myapp.gallery.util.Utils


@Composable
fun MediaListScreen(
    viewModel: MediaListViewModel = hiltViewModel(),
    albumId: Long,
    albumName: String,
    navController: NavHostController
) {


    LaunchedEffect(Unit) {
        if (viewModel.medias.value == Resource.Empty) {
            viewModel.fetchMedias(albumId)
        }
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
                    onItemClick = {
                        selectedMedia = it
                    },
                    onRetryClick = { viewModel.fetchMedias(albumId) })


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
    onItemClick: (Media) -> Unit,
    onRetryClick: () -> Unit
) {
    when (mediasResource) {
        Resource.Loading -> {
            CircularProgressIndicator(modifier = Modifier.testTag("LoadingIndicator"))
        }

        is Resource.Error -> {
            ErrorMessage(mediasResource.message, onRetryClick)
        }

        is Resource.Success -> {
            MediaList(mediasResource.data, onItemClick)
        }

        Resource.Empty -> {}
    }
}

@Composable
fun MediaList(medias: List<Media>, onClickItem: (Media) -> Unit) {

    val listState = rememberLazyGridState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            state = listState,
            columns = GridCells.Adaptive(minSize = 80.dp),
            modifier = Modifier

                .fillMaxSize()
                .testTag("MediaList")
                .clipToBounds(),
            contentPadding = PaddingValues(horizontal = 1.dp, vertical = 1.dp)

        ) {

            items(medias, key = { it.id }) { media ->
                MediaItem(media, onClickItem)
            }
        }


    }

}


@Composable
fun MediaItem(media: Media, onAlbumClick: (Media) -> Unit) {


    // val url = remember(media) { media.getUri().toString() }

    Box(
        modifier = Modifier
            .fillMaxWidth()

            .aspectRatio(1f)
            .padding(0.75.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh
            )

    ) {


        AsyncImage(
            modifier = Modifier
                .testTag("Media_" + media.id)
                .fillMaxSize()
//                .border(
//                    width = 1.dp,
//                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
//                    //shape = RoundedCornerShape(12.dp)
//                )

                .clickable {
                    onAlbumClick(media)
                },
            uri = media.uri.toString(),
            contentDescription = media.name,
            contentScale = ContentScale.Crop,
            sketch = SingletonSketch.get(LocalPlatformContext.current),
            filterQuality = FilterQuality.None,
            clipToBounds = true
        )


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, onBackClick: () -> Unit) {

    TopAppBar(
        title = {
            Text(
                modifier = Modifier.testTag("MediaListTitle"),
                text = title
            )
        },

        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },

        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Menu"
                )
            }
        }
    )

}


