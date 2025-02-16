package com.myapp.gallery.screens.albums

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.GridOn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.SingletonSketch
import com.github.panpf.sketch.request.ImageRequest
import com.github.panpf.sketch.state.ThumbnailMemoryCacheStateImage
import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.state.LayoutOrientation
import com.myapp.gallery.domain.state.SortType
import com.myapp.gallery.ui.components.ErrorMessage
import com.myapp.gallery.ui.components.ShimmerLoader
import com.myapp.gallery.ui.util.ContentDescriptions
import com.myapp.gallery.ui.util.TestTag
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AlbumsScreen(viewModel: AlbumsViewModel = hiltViewModel(),
                 navController: NavHostController
) {

    // Collect UI state from ViewModel
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.testTag(TestTag.ALBUMS_SCREEN),
        topBar = {
            TopBar(title = "Albums",
                uiState = uiState,
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
                    uiState = uiState,
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

            // Show shimmer effect while loading
            ShimmerGridLoader(
                modifier = Modifier.testTag(TestTag.LOADING_INDICATOR)
            )
        }

        is AlbumsUiState.Error -> {

            // Show error message with retry button
            ErrorMessage(uiState.message, onRetryClick = onRetryClick)
        }

        is AlbumsUiState.Success -> {

            // Display album list in either Grid or List format

            when (uiState.layoutOrientation) {
                LayoutOrientation.LIST -> AlbumList(uiState.albums, onAlbumClick)
                LayoutOrientation.GRID -> AlbumGrid(uiState.albums, onAlbumClick)
            }

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
                modifier = Modifier.testTag(TestTag.TOP_BAR), text = title
            )
        },

            actions = {

                // Toggle Grid/List View Button
                IconButton(onClick = {
                    onLayoutSelected()
                }) {
                    Icon(
                        imageVector = if (uiState.layoutOrientation == LayoutOrientation.GRID)
                            Icons.AutoMirrored.Outlined.ViewList
                        else
                            Icons.Outlined.GridOn,
                        contentDescription = ContentDescriptions.TOGGLE_VIEW
                    )
                }

                // Sort Menu Dropdown
                var expanded by remember { mutableStateOf(false) }

                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = ContentDescriptions.MENU_ITEM
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
                text = { Text(text = "Sort by ${sort.name.lowercase()}") },
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
            .testTag(TestTag.ALBUM_GRID),
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
        modifier = Modifier
            .fillMaxSize()
            .testTag(TestTag.ALBUM_LIST),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)

    ) {
        items(albums, key = { it.id }) { album ->
            AlbumItemList(album, onAlbumClick)
        }
    }
}



@Composable
fun AlbumItemGrid(album: Album, onAlbumClick: (Album) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .testTag(TestTag.ALBUM_ITEM_PREFIX + album.id)
    ) {

        AlbumImage(album.thumbnailUri.toString(),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            onItemClick = {
                onAlbumClick(album)
            })

        Spacer(modifier = Modifier.height(8.dp))

        AlbumInfo(name = album.name, itemCount = album.itemCount)

    }
}


@Composable
fun AlbumItemList(album: Album, onAlbumClick: (Album) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .testTag(TestTag.ALBUM_ITEM_PREFIX + album.id)
            .clickable(
                onClick = {
                    onAlbumClick(album)
                }
            )
    ) {

        AlbumImage(album.thumbnailUri.toString(),
            modifier = Modifier
                .width(120.dp)
                .aspectRatio(1f),
            onItemClick = {
                // full item clickable
            })

        Spacer(modifier = Modifier.width(8.dp))

        AlbumInfo(
            name = album.name, itemCount = album.itemCount,
            modifier = Modifier.align(Alignment.CenterVertically)
        )


    }
}

@Composable
fun AlbumImage(
    uri: String,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit
) {


    val context = LocalContext.current
    val sketch = remember { SingletonSketch.get(context) }


    Box(
        modifier = modifier
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
                    onItemClick()
                }
                .clip(RoundedCornerShape(12.dp)),

             uri = uri,
            contentDescription = ContentDescriptions.ALBUM_IMAGE,
            contentScale = ContentScale.Crop,
            sketch = sketch
        )

    }

}


@Composable
fun AlbumInfo(
    name: String,
    itemCount: Int,
    modifier: Modifier = Modifier
) {
    val formattedCount =
        NumberFormat.getNumberInstance(Locale.US).format(itemCount)

    Column(modifier) {
        Text(text = name, fontWeight = FontWeight.Medium)
        Text(
            text = formattedCount, modifier = Modifier.alpha(0.5f)
        )
    }
}

@Composable
fun ShimmerGridLoader(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 120.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(top = 80.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        items(12) {
            ShimmerLoader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray.copy(alpha = 0.3f))

                )
            }
        }
    }
}