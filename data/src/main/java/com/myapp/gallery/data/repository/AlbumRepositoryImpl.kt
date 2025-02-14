package com.myapp.gallery.data.repository

import com.myapp.gallery.data.local.mediastore.MediaStoreDataSource
import com.myapp.gallery.data.local.mediastore.toAlbum
import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.repository.AlbumRepository
import com.myapp.gallery.domain.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

class AlbumRepositoryImpl(
    private val mediaStoreDataSource: MediaStoreDataSource,
    private val coroutineContext: CoroutineContext
) : AlbumRepository {
    override suspend fun getAlbums(): Flow<Resource<List<Album>>> {

        return flow {

            val result = mediaStoreDataSource.getAlbums();

            result.onSuccess { mediaItems ->
                val albums = mediaItems.map { it.toAlbum() }
                emit(Resource.Success(albums))
            }.onFailure {
                emit(Resource.Error("Unexpected error occurred while fetching albums"))
            }

        }.flowOn(coroutineContext)


    }

}