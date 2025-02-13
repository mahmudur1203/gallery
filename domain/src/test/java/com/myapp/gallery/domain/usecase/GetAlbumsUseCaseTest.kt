package com.myapp.gallery.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.repository.AlbumRepository
import com.myapp.gallery.domain.state.Resource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetAlbumsUseCaseTest {

    private val albumRepository = mock<AlbumRepository>()
    private lateinit var getAlbumUseCase: GetAlbumsUseCase


    private val successResult = Resource.Success<List<Album>>(listOf(mock()))
    private val errorResult = Resource.Error("Error")

    @Test
    fun `albums data should be fetched from albumRepository`(): Unit = runBlocking {

        getAlbumUseCase = mockSuccessfulCase()

        getAlbumUseCase()

        verify(albumRepository, times(1)).getAlbums()

    }

    @Test
    fun `emit success result when resource is success`(): Unit = runBlocking {

        getAlbumUseCase = mockSuccessfulCase()

        assertThat(getAlbumUseCase().first()).isEqualTo(successResult)

    }



    @Test
    fun `emit error result when resource is error`(): Unit = runBlocking {

        getAlbumUseCase = mockErrorCase()

        assertThat(getAlbumUseCase().first()).isEqualTo(errorResult)

    }

    private suspend fun mockSuccessfulCase() : GetAlbumsUseCase {
        whenever(albumRepository.getAlbums()).thenReturn(
            flow { emit(successResult) }
        )

        return GetAlbumsUseCase(albumRepository)
    }

    private suspend fun mockErrorCase() : GetAlbumsUseCase {
        whenever(albumRepository.getAlbums()).thenReturn(
            flow { emit(errorResult) }
        )

        return GetAlbumsUseCase(albumRepository)
    }

}