package com.myapp.gallery.data.repository

import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.repository.AlbumRepository
import com.myapp.gallery.domain.state.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AlbumRepositoryImpl : AlbumRepository {
    override suspend fun getAlbums(): Flow<Resource<List<Album>>> {

        // TODO: Replace with original data

        return flow {
            emit(Resource.Success(listOf(
                Album("Album 1", 100, "https://picsum.photos/id/100/200/300"),
                Album("Album 2", 200, "https://picsum.photos/id/200/200/300"),
                Album("Album 3", 300, "https://picsum.photos/id/300/200/300"),
                Album("Album 4", 400, "https://picsum.photos/id/400/200/300"),
                Album("Album 5", 500, "https://picsum.photos/id/500/200/300"),
                Album("Album 6", 600, "https://picsum.photos/id/600/200/300"),
                Album("Album 7", 700, "https://picsum.photos/id/700/200/300"),
                Album("Album 8", 800, "https://picsum.photos/id/800/200/300"),
                Album("Album 9", 900, "https://picsum.photos/id/900/200/300"),
                Album("Album 10", 1000, "https://picsum.photos/id/1000/200/300"),
            )))
        }

    }

}