package com.myapp.gallery.domain.usecase

import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.repository.GalleryRepository
import com.myapp.gallery.domain.state.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlbumsUseCase @Inject constructor(private val galleryRepository: GalleryRepository) {

    suspend operator fun invoke(): Flow<Resource<List<Album>>> {
        return galleryRepository.getAlbums()
    }

}