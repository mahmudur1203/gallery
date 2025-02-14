package com.myapp.gallery.domain.repository

import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.model.Media
import com.myapp.gallery.domain.state.Resource
import kotlinx.coroutines.flow.Flow

interface GalleryRepository {
    suspend fun getAlbums(): Flow<Resource<List<Album>>>
    suspend fun getMediaListByAlbumId(albumId: Long): Flow<Resource<List<Media>>>
}