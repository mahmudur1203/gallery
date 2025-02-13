package com.myapp.gallery.ui.albums

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.state.Resource
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AlbumsScreen(viewModel: AlbumsViewModel = hiltViewModel()) {

    val albums by viewModel.albums.collectAsState()

    AlbumsScreenContent(albums,
        onAlbumClick = {},
        onRetryClick = { viewModel.fetchAlbums() })

    // Content(message = message, onClickButton = { viewModel.message.value = "Button clicked clicked" })
}

@Composable
fun AlbumsScreenContent(
    albumsResource: Resource<List<Album>>,
    onAlbumClick: (Album) -> Unit,
    onRetryClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TopBar(title = "Albums")
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
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
        }
    )
}

@Composable
private fun ErrorMessage(
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
fun TopBar(title: String) {

    TopAppBar(
        title = {
            Text(text = title)
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
            .clickable {
                onAlbumClick(album)
            }
            .testTag("AlbumItem_" + album.name)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.LightGray)
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Album Thumbnail",
                contentScale = ContentScale.Crop,
                painter = rememberAsyncImagePainter(album.thumbnailUri),
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