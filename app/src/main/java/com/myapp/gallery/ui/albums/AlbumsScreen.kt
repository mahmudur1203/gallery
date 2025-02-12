package com.myapp.gallery.ui.albums

import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.state.Resource

@Composable
fun GalleryScreen(viewModel: AlbumsViewModel = hiltViewModel(),

                  ) {

    LaunchedEffect(Unit) {
        viewModel.fetchAlbums()
    }

    val albums by viewModel.albums.collectAsState()

    Scaffold(
        topBar = {
            TopBar(title = "Gallery")
        },
        content =  { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {

                if(albums == Resource.Loading) {
                    CircularProgressIndicator()
                }else if(albums is Resource.Error) {
                    Text(
                        text = "Error: ${(albums as Resource.Error).message}",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }else if(albums is Resource.Success) {
                    AlbumList(((albums as Resource.Success<List<Album>>).data as List<Album>))
                }
                else if(albums == null) {

                }



            }
        }
    )


   // Content(message = message, onClickButton = { viewModel.message.value = "Button clicked clicked" })
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title:String){

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
fun AlbumList(albums: List<Album>) {
    LazyColumn (
        modifier = Modifier.fillMaxSize()
    ) {
        items(albums) { album ->
            AlbumItem(album)
        }
    }
}

@Composable
fun AlbumItem(album: Album) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ){
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = album.name.substring(0,1)
                        .uppercase(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }


            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = album.name, fontWeight = FontWeight.Bold)
                Text(text = "${album.itemCount} items")
            }
        }
    }
}