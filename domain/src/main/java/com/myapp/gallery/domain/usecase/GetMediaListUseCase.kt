package com.myapp.gallery.domain.usecase

import com.myapp.gallery.domain.model.Media
import com.myapp.gallery.domain.repository.GalleryRepository
import com.myapp.gallery.domain.state.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMediaListUseCase @Inject constructor(private val galleryRepository: GalleryRepository) {

    suspend operator fun invoke(albumId: Long): Flow<Resource<List<Media>>> {
        return galleryRepository.getMediaListByAlbumId(albumId)
    }

}