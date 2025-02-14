package com.myapp.gallery.screens.albums

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.LocalPlatformContext
import com.github.panpf.sketch.SingletonSketch
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.state.Resource
import com.myapp.gallery.ui.theme.GalleryTheme
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AlbumsScreen(viewModel: AlbumsViewModel = hiltViewModel(),
                 navController: NavHostController
) {

    LaunchedEffect(Unit) {
        if (viewModel.albums.value == Resource.Empty) {
            viewModel.fetchAlbums()
        }
    }

    val albumsResource by viewModel.albums.collectAsState()

    Scaffold(
        topBar = {
            TopBar(title = "Albums", onBackClick = { navController.popBackStack() })
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {

                AlbumsScreenContent(albumsResource,
                    onAlbumClick = {
                        navController.navigate("media_list/${it.id}/${it.name}")
                    },
                    onRetryClick = { viewModel.fetchAlbums() })

            }
        }
    )

}

@Composable
fun AlbumsScreenContent(
    albumsResource: Resource<List<Album>>,
    onAlbumClick: (Album) -> Unit,
    onRetryClick: () -> Unit
) {

    when (albumsResource) {
        Resource.Loading -> {
            CircularProgressIndicator(modifier = Modifier.testTag("LoadingIndicator"))
        }

        is Resource.Error -> {
            ErrorMessage(albumsResource.message, onRetryClick)
        }

        is Resource.Success -> {
            AlbumList(albumsResource.data, onAlbumClick)
        }

        Resource.Empty -> {}
    }

}

@Composable
fun ErrorMessage(
    message: String,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .testTag("ErrorMessage")
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            text = message,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))


        Button(
            modifier = Modifier.testTag("RetryButton"),
            onClick = { onRetryClick() }) {
            Text(
                text = "Tap to retry"
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
                modifier = Modifier.testTag("AlbumTitle"),
                text = title
            )
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

@Composable
fun AlbumList(albums: List<Album>, onAlbumClick: (Album) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .testTag("AlbumList"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)

    ) {
        items(albums) { album ->
            AlbumItem(album, onAlbumClick)
        }
    }
}

@Composable
fun AlbumItem(album: Album, onAlbumClick: (Album) -> Unit) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)

            .testTag("AlbumItem_" + album.name)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {

            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(12.dp)
                    ).clickable {
                        onAlbumClick(album)
                    }
                    .clip(RoundedCornerShape(12.dp)),

                uri = album.thumbnailUri.toString(),
                contentDescription = album.name,
                contentScale = ContentScale.Crop,
                sketch = SingletonSketch.get(LocalPlatformContext.current)
            )

        }

        Spacer(modifier = Modifier.height(8.dp))

        val formattedCount =
            NumberFormat.getNumberInstance(Locale.US).format(album.itemCount)

        Column {
            Text(text = album.name, fontWeight = FontWeight.Medium)
            Text(text = formattedCount)
        }
    }
}

@Preview
@Composable
fun AlbumsScreenPreview() {
    GalleryTheme {
        AlbumList(albums = listOf(
            Album(id = 1, name = "Album 1", itemCount = 100, thumbnailUri = ""),
            Album(id = 2, name = "Album 2", itemCount = 200, thumbnailUri = ""),
            Album(id = 3, name = "Album 3", itemCount = 300, thumbnailUri = ""),
        ), onAlbumClick = {})

    }
}