package com.myapp.gallery.di

import android.content.Context
import com.myapp.gallery.data.local.mediastore.MediaStoreDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    fun provideMediaDataSource(@ApplicationContext context: Context): MediaStoreDataSource {
        return MediaStoreDataSource(context)
    }

}