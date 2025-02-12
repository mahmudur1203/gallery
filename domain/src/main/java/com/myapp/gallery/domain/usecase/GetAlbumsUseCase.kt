package com.myapp.gallery.domain.usecase

import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.state.Resource
import kotlinx.coroutines.delay
import javax.inject.Inject

class GetAlbumsUseCase @Inject constructor() {

    suspend operator fun invoke(): Resource<List<Album>> {

        delay(2000)

        // Todo: Replace with real implementation

        return Resource.Success(listOf(
            Album("Album 1", 100, "https://picsum.photos/id/100/200/300"),
            Album("Album 2", 200, "https://picsum.photos/id/200/200/300")))

    }

}