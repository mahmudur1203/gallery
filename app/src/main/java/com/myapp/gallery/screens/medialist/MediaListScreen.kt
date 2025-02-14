package com.myapp.gallery.screens.medialist

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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ArrowBack
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                    onAlbumClick = {
                        // todo: navigate to media
                    },
                    onRetryClick = { viewModel.fetchMedias(albumId) })

            }
        }
    )

}

@Composable
fun MediaListScreenContent(
    mediasResource: Resource<List<Media>>,
    onAlbumClick: (Media) -> Unit,
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
            MediaList(mediasResource.data, onAlbumClick)
        }

        Resource.Empty -> {}
    }
}

@Composable
fun MediaList(medias: List<Media>, onAlbumClick: (Media) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
            .fillMaxSize()
            .testTag("MediaList"),
        contentPadding = PaddingValues(horizontal = 1.dp, vertical = 1.dp)

    ) {
        items(medias) { album ->
            MediaItem(album, onAlbumClick)
        }
    }
}

@Composable
fun MediaItem(media: Media, onAlbumClick: (Media) -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(0.75.dp)
    ) {


        AsyncImage(
            modifier = Modifier.testTag("Media_" + media.id)
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
            sketch = SingletonSketch.get(LocalPlatformContext.current)
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
                    imageVector = Icons.Outlined.ArrowBack,
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


