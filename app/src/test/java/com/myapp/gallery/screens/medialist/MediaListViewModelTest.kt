package com.myapp.gallery.screens.medialist

import com.google.common.truth.Truth.assertThat
import com.myapp.gallery.BaseUnitTest
import com.myapp.gallery.domain.model.Media
import com.myapp.gallery.domain.state.Resource
import com.myapp.gallery.domain.usecase.GetMediaUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

class MediaListViewModelTest : BaseUnitTest(){

    private lateinit var viewModel: MediaListViewModel
    private val getMediaUseCase = mock<GetMediaUseCase>()

    private val successResult = Resource.Success<List<Media>>(listOf(mock()))
    private val errorResult = Resource.Error("Error")

    @Test
    fun `media items should be fetched from getMediaUseCase`(): Unit = runBlocking {

        viewModel = MediaListViewModel(getMediaUseCase)

        viewModel.fetchMedias(1)

        verify(getMediaUseCase,times(1)).invoke(1)

    }

    @Test
    fun `fetchMedias should update state with success when media items are available`(): Unit = runBlocking {

        viewModel = mockSuccessfulCase()

        viewModel.fetchMedias(1)

        assertThat(viewModel.medias.first()).isEqualTo(successResult)

    }

    @Test
    fun `fetchMedias should update state with error when media items are not available`(): Unit = runBlocking {

        viewModel = mockErrorCase()

        viewModel.fetchMedias(1)

        assertThat(viewModel.medias.first()
            ).isEqualTo(errorResult)

    }

    private suspend fun mockErrorCase(): MediaListViewModel {
        whenever(getMediaUseCase.invoke(1)).thenReturn(
            flow { emit(errorResult) })

        return MediaListViewModel(getMediaUseCase)

    }


    private suspend fun mockSuccessfulCase() : MediaListViewModel {

        whenever(getMediaUseCase.invoke(1)).thenReturn(
            flow { emit(successResult) })

        return MediaListViewModel(getMediaUseCase)
    }


}