package com.myapp.gallery.ui.albums

import com.google.common.truth.Truth.assertThat
import com.myapp.gallery.BaseUnitTest
import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.state.Resource
import com.myapp.gallery.domain.usecase.GetAlbumsUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class AlbumsViewModelTest : BaseUnitTest() {

    private lateinit var viewModel: AlbumsViewModel
    private val getAlbumsUseCase = mock<GetAlbumsUseCase>()

    private val successResult = Resource.Success<List<Album>>(listOf(mock()))
    private val errorResult = Resource.Error("Error")


    @Test
    fun `getAlbumsUseCase should be called single time when fetchAlbums is called`(): Unit = runBlocking {

        viewModel = AlbumsViewModel(getAlbumsUseCase)

        // fetchAlbums is called when the viewModel is created
        //viewModel.fetchAlbums()

        verify(getAlbumsUseCase,times(1)).invoke()

    }

    @Test
    fun `fetchAlbums should update state with success when albums are available`(): Unit = runBlocking {

        whenever(getAlbumsUseCase.invoke()).thenReturn(
            successResult)

        viewModel = AlbumsViewModel(getAlbumsUseCase)

        viewModel.fetchAlbums()

        assertThat(viewModel.albums.first()).isEqualTo(successResult)


    }

    @Test
    fun `fetchAlbums should update state with error when albums are not available`(): Unit = runBlocking {

        whenever(getAlbumsUseCase.invoke()).thenReturn(
            errorResult)

        viewModel = AlbumsViewModel(getAlbumsUseCase)

        viewModel.fetchAlbums()

        assertThat(viewModel.albums.first()
            ).isEqualTo(errorResult)

    }



}

