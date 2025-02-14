package com.myapp.gallery.data.local.mediastore

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.myapp.gallery.data.local.mediastore.model.MediaFile
import com.myapp.gallery.data.local.mediastore.model.MediaFolder

class MediaStoreDataSource(private val context:Context) {

    suspend fun getAlbums() : Result<MutableList<MediaFolder>> {

         return runCatching {
            val albumMap = mutableMapOf<Long, MediaFolder>()
            val allImages = mutableListOf<Uri>()
            val allVideos = mutableListOf<Uri>()

            fetchMediaFromStore(
                context,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                albumMap,
                allImages
            )
            fetchMediaFromStore(
                context,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                albumMap,
                allVideos
            )

            val sortedAlbums = albumMap.values.toList().sortedBy { it.name }
            val finalAlbums = sortedAlbums.toMutableList()

            if (allImages.isNotEmpty()) {
                finalAlbums.add(
                    MediaFolder(
                        id = -1,
                        name = "All Images",
                        itemCount = allImages.size,
                        thumbnailUri = allImages.first().toString(),
                    )
                )
            }

            if (allVideos.isNotEmpty()) {
                finalAlbums.add(
                    MediaFolder(
                        id = -2,
                        name = "All Videos",
                        itemCount = allVideos.size,
                        thumbnailUri = allVideos.first().toString(),

                        )
                )
            }

            finalAlbums
        }

    }

    suspend fun getMediaForAlbum(albumId: Long): Result<List<MediaFile>> {

        return runCatching {
            val mediaList = mutableListOf<MediaFile>()

            when (albumId) {
                -1L -> { // ✅ Fetch ALL Images & Videos
                    mediaList.addAll(fetchMedia(context,MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
              //      mediaList.addAll(fetchMedia(context,MediaStore.Video.Media.EXTERNAL_CONTENT_URI, isVideo = true))
                }
                -2L -> { // ✅ Fetch ALL Videos
                    mediaList.addAll(fetchMedia(context,MediaStore.Video.Media.EXTERNAL_CONTENT_URI, isVideo = true))
                }
                else -> { // ✅ Fetch from Specific Album
                    mediaList.addAll(fetchMedia(context,MediaStore.Images.Media.EXTERNAL_CONTENT_URI, albumId))
                    mediaList.addAll(fetchMedia(context,MediaStore.Video.Media.EXTERNAL_CONTENT_URI, albumId, isVideo = true))
                }
            }
            mediaList
        }

    }




}