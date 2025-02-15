package com.myapp.gallery.screens.albums

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.LocalPlatformContext
import com.github.panpf.sketch.SingletonSketch
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.state.LayoutOrientation
import com.myapp.gallery.domain.state.SortType
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AlbumsScreen(viewModel: AlbumsViewModel = hiltViewModel(),
                 navController: NavHostController
) {


    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(title = "Albums",
                uiState,
                onSortSelected = { viewModel.updateSortType(it) },
                onLayoutSelected = { viewModel.toggleLayoutType() },
                onBackClick = { navController.popBackStack() })
        },


        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {

                AlbumsScreenContent(
                    uiState,
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
    uiState: AlbumsUiState,
    onAlbumClick: (Album) -> Unit,
    onRetryClick: () -> Unit
) {

    when (uiState) {
        AlbumsUiState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.testTag("LoadingIndicator"))
        }

        is AlbumsUiState.Error -> {
            ErrorMessage(uiState.message, onRetryClick)
        }

        is AlbumsUiState.Success -> {

            if (uiState.layoutOrientation == LayoutOrientation.LIST) {
                AlbumList(uiState.albums, onAlbumClick)
            } else {
                AlbumGrid(uiState.albums, onAlbumClick)
            }

        }

        else -> {}
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
fun TopBar(
    title: String,
    uiState: AlbumsUiState,
    onSortSelected: (SortType) -> Unit,
    onLayoutSelected: () -> Unit,
    onBackClick: () -> Unit
) {

    if (uiState is AlbumsUiState.Success) {
        TopAppBar(title = {
            Text(
                modifier = Modifier.testTag("AlbumTitle"), text = title
            )
        },

            actions = {
                IconButton(onClick = {
                    onLayoutSelected()
                }) {
                    Icon(
                        imageVector = if (uiState.layoutOrientation == LayoutOrientation.GRID) Icons.AutoMirrored.Outlined.ViewList else Icons.Filled.GridView,
                        contentDescription = "Toggle View"
                    )
                }

                var expanded by remember { mutableStateOf(false) }

                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert, contentDescription = "Toggle View"
                    )
                }

                if (expanded) {
                    SortOptions(onSortSelected = {
                        onSortSelected(it)
                        expanded = false
                    }, onDismiss = {
                        expanded = false
                    })
                }

            })
    }
}

@Composable
fun SortOptions(
    onSortSelected: (SortType) -> Unit, onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = true,
        onDismissRequest = { onDismiss() },
    ) {
        availableSortTypes.forEach { sort ->

            DropdownMenuItem(
                onClick = {

                    onSortSelected(sort)

                },
                text = { Text(text = "Sort by" + " " + sort.name.lowercase()) },
            )
        }
    }
}



@Composable
fun AlbumGrid(albums: List<Album>, onAlbumClick: (Album) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 120.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("AlbumList"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)

    ) {
        items(albums, key = { it.id }) { album ->
            AlbumItemGrid(album, onAlbumClick)
        }
    }
}

@Composable
fun AlbumList(albums: List<Album>, onAlbumClick: (Album) -> Unit) {
    LazyColumn(
        //columns =GridCells.Adaptive(minSize = 120.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("AlbumList"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)

    ) {
        items(albums, key = { it.id }) { album ->
            AlbumItemList(album, onAlbumClick)
        }
    }
}

@Composable
fun AlbumItemList(album: Album, onAlbumClick: (Album) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .testTag("AlbumItem_" + album.name)
            .clickable(
                onClick = {
                    onAlbumClick(album)
                }
            )
    ) {
        Box(
            modifier = Modifier
                .width(120.dp)
                .aspectRatio(1f)
        ) {

            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        //onAlbumClick(album)
                    }
                    .clip(RoundedCornerShape(12.dp)),

                uri = album.thumbnailUri.toString(),
                contentDescription = album.name,
                contentScale = ContentScale.Crop,
                sketch = SingletonSketch.get(LocalPlatformContext.current)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        val formattedCount =
            NumberFormat.getNumberInstance(Locale.US).format(album.itemCount)

        Column(modifier = Modifier.align(Alignment.CenterVertically)) {
            Text(text = album.name, fontWeight = FontWeight.Medium)
            Text(
                text = formattedCount, modifier = Modifier.alpha(0.5f)
            )
        }
    }
}

@Composable
fun AlbumItemGrid(album: Album, onAlbumClick: (Album) -> Unit) {


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
                    )
                    .clickable {
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
            Text(
                text = formattedCount, modifier = Modifier.alpha(0.5f)
            )
        }
    }
}

