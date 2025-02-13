package com.myapp.gallery.domain.usecase

import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.repository.AlbumRepository
import com.myapp.gallery.domain.state.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAlbumsUseCase @Inject constructor(private val albumRepository: AlbumRepository) {

    suspend operator fun invoke(): Flow<Resource<List<Album>>> {
        return albumRepository.getAlbums()
    }

}