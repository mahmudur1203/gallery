package com.myapp.gallery.domain.repository

import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.state.Resource
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    suspend fun getAlbums(): Flow<Resource<List<Album>>>
}