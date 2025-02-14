package com.myapp.gallery.di

import com.myapp.gallery.data.local.mediastore.MediaStoreDataSource
import com.myapp.gallery.data.repository.GalleryRepositoryImpl
import com.myapp.gallery.domain.repository.GalleryRepository
import com.myapp.gallery.util.DispatchersProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModuleModule {

    @Provides
    fun provideAlbumRepository(
        dataSource: MediaStoreDataSource,
        dispatchersProvider: DispatchersProvider
    ): GalleryRepository {
        return GalleryRepositoryImpl(
            mediaStoreDataSource = dataSource,
            coroutineContext = dispatchersProvider.io
        )
    }


}