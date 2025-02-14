package com.myapp.gallery.data.repository

import com.myapp.gallery.data.local.mediastore.MediaStoreDataSource
import com.myapp.gallery.data.local.mediastore.toAlbum
import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.model.Media
import com.myapp.gallery.domain.repository.GalleryRepository
import com.myapp.gallery.domain.state.Resource
import kotlinx.coroutines.delay
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
        delay(2000)

        return flow {
            emit(
                Resource.Success(
                    listOf(
                        Media(
                            1, "https://picsum.photos/id/100/200/300", "Image 1", 100, false, null
                        ), Media(
                            2, "https://picsum.photos/id/200/200/300", "Image 2", 200, false, null
                        ), Media(
                            3, "https://picsum.photos/id/300/200/300", "Image 3", 300, false, null
                        ), Media(
                            4, "https://picsum.photos/id/400/200/300", "Image 4", 400, false, null
                        ), Media(
                            5, "https://picsum.photos/id/500/200/300", "Image 5", 500, false, null
                        ), Media(
                            6, "https://picsum.photos/id/600/200/300", "Image 6", 600, false, null
                        ), Media(
                            7, "https://picsum.photos/id/700/200/300", "Image 7", 700, false, null
                        ), Media(
                            8, "https://picsum.photos/id/800/200/300", "Image 8", 800, false, null
                        ), Media(
                            9, "https://picsum.photos/id/900/200/300", "Image 9", 900, false, null
                        ), Media(
                            10,
                            "https://picsum.photos/id/1000/200/300",
                            "Image 10",
                            1000,
                            false,
                            null
                        )

                    )
                )
            )

        }
    }

}