package com.myapp.gallery.di

import android.app.Application
import android.content.Context
import com.myapp.gallery.util.DispatchersProvider
import com.myapp.gallery.util.DispatchersProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideDispatchersProvider(): DispatchersProvider {
        return DispatchersProviderImpl()
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

}