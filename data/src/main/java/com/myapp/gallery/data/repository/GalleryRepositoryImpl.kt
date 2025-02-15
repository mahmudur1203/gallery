package com.myapp.gallery.data.repository

import com.myapp.gallery.data.local.mediastore.MediaStoreDataSource
import com.myapp.gallery.data.local.mediastore.model.toAlbum
import com.myapp.gallery.data.local.mediastore.model.toMedia
import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.model.Media
import com.myapp.gallery.domain.repository.GalleryRepository
import com.myapp.gallery.domain.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.CoroutineContext

class GalleryRepositoryImpl(
    private val mediaStoreDataSource: MediaStoreDataSource,
    private val coroutineContext: CoroutineContext
) : GalleryRepository {
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

    override suspend fun getMediaListByAlbumId(albumId: Long): Flow<Resource<List<Media>>> {
       return flow {
           val result = mediaStoreDataSource.getMediaForAlbum(albumId)

           result.onSuccess { mediaItems ->
               val albums = mediaItems.map { it.toMedia() }
               emit(Resource.Success(albums))
           }.onFailure {
               emit(Resource.Error("Unexpected error occurred while fetching media"))
           }
       }.flowOn(coroutineContext)
    }

}