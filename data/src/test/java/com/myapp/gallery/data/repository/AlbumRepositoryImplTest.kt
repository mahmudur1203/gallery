package com.myapp.gallery.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.myapp.gallery.data.CoroutineTestRule
import com.myapp.gallery.data.local.mediastore.MediaStoreDataSource
import com.myapp.gallery.domain.repository.AlbumRepository
import com.myapp.gallery.domain.state.Resource
import com.myapp.gallery.testing.data.mediaFolderListTestData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class AlbumRepositoryImplTest{

    @get:Rule
    val coroutinesRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var albumRepository : AlbumRepository

    private val mediaStoreDataSource  = mock<MediaStoreDataSource>()
    private val exception = Exception("Error")


    @Test
    fun `media item should be fetched from mediaStoreDataSource`() : Unit = runBlocking {

        albumRepository = mockErrorCase()

        albumRepository.getAlbums().first()

        verify(mediaStoreDataSource,times(1)).getAlbums()

    }

    @Test
    fun `emit success when media item fetched successfully from mediaStoreDataSource`() : Unit = runBlocking {

        albumRepository = mockSuccessfulCase()

        val result = albumRepository.getAlbums().first()

        assertThat(result.isSuccess).isTrue()

        if (result is Resource.Success) {
            assertThat(result.data.size).isEqualTo(mediaFolderListTestData.size)
        }

    }

    @Test
    fun `emit error when mediaStoreDataSource throws exception`() = runBlocking{

        albumRepository = mockErrorCase()

        val result = albumRepository.getAlbums().first()

        assertThat(result.isError).isTrue()

    }

    private suspend fun mockSuccessfulCase() : AlbumRepository {

        whenever(mediaStoreDataSource.getAlbums()).thenReturn(
            Result.success(mediaFolderListTestData)
        )

        return  AlbumRepositoryImpl(
            mediaStoreDataSource,
            coroutinesRule.dispatcher
        )
    }

    private suspend fun mockErrorCase() : AlbumRepository {

        whenever(mediaStoreDataSource.getAlbums()).thenReturn(
            Result.failure(exception)
        )

        return AlbumRepositoryImpl(mediaStoreDataSource,
            coroutinesRule.dispatcher)

    }

}