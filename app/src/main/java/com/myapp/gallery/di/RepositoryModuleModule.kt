package com.myapp.gallery.di

import com.myapp.gallery.data.local.mediastore.MediaStoreDataSource
import com.myapp.gallery.data.repository.AlbumRepositoryImpl
import com.myapp.gallery.domain.repository.AlbumRepository
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
    ): AlbumRepository {
        return AlbumRepositoryImpl(
            mediaStoreDataSource = dataSource,
            coroutineContext = dispatchersProvider.io
        )
    }


}