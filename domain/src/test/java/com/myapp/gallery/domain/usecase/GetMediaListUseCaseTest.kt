package com.myapp.gallery.domain.usecase

import com.google.common.truth.Truth
import com.myapp.gallery.domain.model.Media
import com.myapp.gallery.domain.repository.GalleryRepository
import com.myapp.gallery.domain.state.Resource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetMediaListUseCaseTest{

    private val galleryRepository = mock<GalleryRepository>()
    private lateinit var getMediaListUseCase: GetMediaListUseCase


    private val successResult = Resource.Success<List<Media>>(listOf(mock()))
    private val errorResult = Resource.Error("Error")

    @Test
    fun `mediaList should be fetched from repository`(): Unit = runBlocking {

        getMediaListUseCase = mockSuccessfulCase()

        getMediaListUseCase(1L)

        verify(galleryRepository, times(1)).getMediaListByAlbumId(1L)

    }

    @Test
    fun `emit success result when resource is success`(): Unit = runBlocking {

        getMediaListUseCase = mockSuccessfulCase()

        Truth.assertThat(getMediaListUseCase(1).first()).isEqualTo(successResult)

    }



    @Test
    fun `emit error result when resource is error`(): Unit = runBlocking {

        getMediaListUseCase = mockErrorCase()

        Truth.assertThat(getMediaListUseCase(1).first()).isEqualTo(errorResult)

    }

    private suspend fun mockSuccessfulCase() : GetMediaListUseCase {
        whenever(galleryRepository.getMediaListByAlbumId(1)).thenReturn(
            flow { emit(successResult) }
        )

        return GetMediaListUseCase(galleryRepository)
    }

    private suspend fun mockErrorCase() : GetMediaListUseCase {
        whenever(galleryRepository.getMediaListByAlbumId(1)).thenReturn(
            flow { emit(errorResult) }
        )

        return GetMediaListUseCase(galleryRepository)
    }

}