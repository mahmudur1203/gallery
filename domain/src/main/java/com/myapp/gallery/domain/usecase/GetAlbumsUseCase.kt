package com.myapp.gallery.domain.usecase

import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.state.Resource
import kotlinx.coroutines.delay
import javax.inject.Inject

class GetAlbumsUseCase @Inject constructor() {

    suspend operator fun invoke(): Resource<List<Album>> {

        delay(2000)

//        if(true){
//            return Resource.Error("Error Test which will be replaced with real implementation")
//        }

        // Todo: Replace with real implementation

        return Resource.Success(listOf(
            Album("Album 1", 100, "https://picsum.photos/id/100/200/300"),
            Album("Album 2", 200, "https://picsum.photos/id/200/200/300"),
            Album("Album 3", 300, "https://picsum.photos/id/300/200/300"),
            Album("Album 4", 400, "https://picsum.photos/id/400/200/300"),
            Album("Album 5", 500, "https://picsum.photos/id/500/200/300"),
            Album("Album 6", 600, "https://picsum.photos/id/600/200/300"),
            Album("Album 7", 700, "https://picsum.photos/id/700/200/300"),
            Album("Album 8", 800, "https://picsum.photos/id/800/200/300"),
            Album("Album 9", 900, "https://picsum.photos/id/900/200/300"),
            Album("Album 10", 1000, "https://picsum.photos/id/1000/200/300"),
            Album("Album 11 ", 1100, "https://picsum.photos/id/1100/200/300"),
            Album("Album 12", 1200, "https://picsum.photos/id/1200/200/300"),
            Album("Album 13", 1300, "https://picsum.photos/id/1300/200/300"),
            Album("Album 14", 1400, "https://picsum.photos/id/1400/200/300"),
            Album("Album 15", 1500, "https://picsum.photos/id/1500/200/300"),
            Album("Album 16", 1600, "https://picsum.photos/id/1600/200/300"),
            Album("Album 17", 1700, "https://picsum.photos/id/1700/200/300"),
            Album("Album 18", 1800, "https://picsum.photos/id/1800/200/300"),
            Album("Album 19", 1900, "https://picsum.photos/id/1900/200/300"),
            Album("Album 20", 2000, "https://picsum.photos/id/300/200/300"),
            Album("Album 21", 2100, "https://picsum.photos/id/2100/200/300"),
            Album("Album 22", 2200, "https://picsum.photos/id/2200/200/300"),
            Album("Album 23", 2300, "https://picsum.photos/id/2300/200/300"),
            Album("Album 24", 2400, "https://picsum.photos/id/2400/200/300"),
            Album("Album 25", 2500, "https://picsum.photos/id/2500/200/300"),
            Album("Album 26", 2600, "https://picsum.photos/id/2600/200/300"),
            Album("Album 27", 2700, "https://picsum.photos/id/2700/200/300"),
            Album("Album 28", 2800, "https://picsum.photos/id/2800/200/300"),
            Album("Album 29", 2900, "https://picsum.photos/id/2900/200/300")))

    }

}