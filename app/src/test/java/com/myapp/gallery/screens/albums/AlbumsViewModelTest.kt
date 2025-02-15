package com.myapp.gallery.screens.albums

import com.google.common.truth.Truth.assertThat
import com.myapp.gallery.BaseUnitTest
import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.state.Resource
import com.myapp.gallery.domain.usecase.GetAlbumsUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
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
    fun `albums data should be fetched from getAlbumsUseCase`(): Unit = runBlocking {

        viewModel = AlbumsViewModel(getAlbumsUseCase)

        // fetchAlbums is called when the viewModel is created
        //viewModel.fetchAlbums()

        verify(getAlbumsUseCase,times(1)).invoke()

    }

    @Test
    fun `fetchAlbums should update state with success when albums are available`(): Unit = runBlocking {

        viewModel=mockSuccessfulCase()

        viewModel.fetchAlbums()

        assertThat(viewModel.uiState.first()).isInstanceOf(AlbumsUiState.Success::class.java)

        assertThat((viewModel.uiState.first() as AlbumsUiState.Success).albums).isEqualTo(successResult.data)


    }


    @Test
    fun `fetchAlbums should update state with error when albums are not available`(): Unit = runBlocking {

        viewModel = mockErrorCase()

        viewModel.fetchAlbums()


        assertThat(viewModel.uiState.first()
            ).isInstanceOf(AlbumsUiState.Error::class.java)

    }

    private suspend fun mockSuccessfulCase() : AlbumsViewModel {
        whenever(getAlbumsUseCase.invoke()).thenReturn(
            flow { emit(successResult) })

        return AlbumsViewModel(getAlbumsUseCase)
    }

    private suspend fun mockErrorCase() : AlbumsViewModel {
        whenever(getAlbumsUseCase.invoke()).thenReturn(
            flow { emit(errorResult) })

        return AlbumsViewModel(getAlbumsUseCase)
    }


}

