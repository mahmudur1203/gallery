package com.myapp.gallery.di

import com.myapp.gallery.data.repository.AlbumRepositoryImpl
import com.myapp.gallery.domain.repository.AlbumRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModuleModule {

    @Provides
    fun provideAlbumRepository(): AlbumRepository {
        return AlbumRepositoryImpl()
    }
}