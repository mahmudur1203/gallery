package com.myapp.gallery.data.local.mediastore

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

class MediaStoreDataSource(private val context:Context) {

    suspend fun getAlbums() : Result<MutableList<MediaItem>> {

         return runCatching {
            val albumMap = mutableMapOf<Long, MediaItem>()
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
                    MediaItem(
                        name = "All Images",
                        itemCount = allImages.size,
                        thumbnailUri = allImages.first().toString(),
                    )
                )
            }

            if (allVideos.isNotEmpty()) {
                finalAlbums.add(
                    MediaItem(
                        name = "All Videos",
                        itemCount = allVideos.size,
                        thumbnailUri = allVideos.first().toString(),

                        )
                )
            }

            finalAlbums
        }


    }

    private fun fetchMediaFromStore(
        context: Context,
        uri: Uri,
        albumMap: MutableMap<Long, MediaItem>,
        allMedia: MutableList<Uri>
    ) {
        runCatching {
            val projection = arrayOf(
                MediaStore.MediaColumns.BUCKET_ID,
                MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
                MediaStore.MediaColumns._ID
            )

            val sortOrder = "${MediaStore.MediaColumns.DATE_MODIFIED} DESC"

            context.contentResolver.query(uri, projection, null, null, sortOrder)?.use { cursor ->
                val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_ID)
                val bucketNameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)
                val mediaIdColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)

                while (cursor.moveToNext()) {
                    val bucketId = cursor.getLong(bucketIdColumn)
                    val bucketName = cursor.getString(bucketNameColumn) ?: "Unknown"
                    val mediaId = cursor.getLong(mediaIdColumn)
                    val mediaUri = ContentUris.withAppendedId(uri, mediaId)



                    allMedia.add(mediaUri)

                    if (albumMap.containsKey(bucketId)) {
                        val updatedAlbum = albumMap[bucketId]!!.copy(itemCount = albumMap[bucketId]!!.itemCount + 1)
                        albumMap[bucketId] = updatedAlbum
                    } else {
                        albumMap[bucketId] = MediaItem(
                            name = bucketName,
                            itemCount = 1,
                            thumbnailUri = mediaUri.toString(),
                        )
                    }
                }
            }
        }
    }

}