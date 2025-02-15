package com.myapp.gallery.data.local.mediastore

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.myapp.gallery.data.local.mediastore.model.MediaFile
import com.myapp.gallery.data.local.mediastore.model.MediaFolder

fun fetchMediaFromStore(
    context: Context,
    uri: Uri,
    albumMap: MutableMap<Long, MediaFolder>,
    allMedia: MutableList<Pair<Uri,Long>>
) {
    runCatching {
        val projection = arrayOf(
            MediaStore.MediaColumns.BUCKET_ID,
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DATE_MODIFIED
        )

        val sortOrder = "${MediaStore.MediaColumns.DATE_MODIFIED} DESC"

        context.contentResolver.query(uri, projection, null, null, sortOrder)?.use { cursor ->
            val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_ID)
            val bucketNameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)
            val mediaIdColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val timestampColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED)

            while (cursor.moveToNext()) {
                val bucketId = cursor.getLong(bucketIdColumn)
                val bucketName = cursor.getString(bucketNameColumn) ?: "Unknown"
                val mediaId = cursor.getLong(mediaIdColumn)
                val mediaUri = ContentUris.withAppendedId(uri, mediaId)
                val timestamp = cursor.getLong(timestampColumn)

                allMedia.add(Pair(mediaUri,timestamp))

                if (albumMap.containsKey(bucketId)) {
                    val updatedAlbum = albumMap[bucketId]!!.copy(itemCount = albumMap[bucketId]!!.itemCount + 1)
                    albumMap[bucketId] = updatedAlbum
                } else {
                    albumMap[bucketId] = MediaFolder(
                        id = bucketId,
                        name = bucketName,
                        itemCount = 1,
                        thumbnailUri = mediaUri.toString(),
                        timestamp = timestamp
                    )
                }
            }
        }
    }
}

fun fetchMedia(context: Context,uri: Uri, albumId: Long? = null, isVideo: Boolean = false): List<MediaFile> {
    val mediaList = mutableListOf<MediaFile>()

    val projectionList = mutableListOf(
        MediaStore.MediaColumns._ID,
        MediaStore.MediaColumns.DISPLAY_NAME,
        MediaStore.MediaColumns.SIZE,
        MediaStore.MediaColumns.DATE_MODIFIED,
        MediaStore.MediaColumns.BUCKET_ID)
        .also {
            if (isVideo) {
                it.add(MediaStore.Video.Media.DURATION)
            }
        }

    val selection = albumId?.let { "${MediaStore.MediaColumns.BUCKET_ID} = ?" }
    val selectionArgs = albumId?.let { arrayOf(it.toString()) }
    val sortOrder = "${MediaStore.MediaColumns.DATE_MODIFIED} DESC"

    context.contentResolver.query(uri, projectionList.toTypedArray(), selection, selectionArgs, sortOrder)?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
        val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)
        val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED)
        val durationColumn = if (isVideo) cursor.getColumnIndex(MediaStore.Video.Media.DURATION) else -1

        while (cursor.moveToNext()) {
            val mediaId = cursor.getLong(idColumn)
            val mediaUri = ContentUris.withAppendedId(uri, mediaId)
            val fileName = cursor.getString(nameColumn) ?: "Unknown"

            val fileSize = cursor.getLong(sizeColumn)

            val timestamp = cursor.getLong(dateColumn) * 1000

            val duration = if (isVideo && durationColumn != -1) cursor.getLong(durationColumn) else null

            mediaList.add(
                MediaFile(
                    id = mediaId,
                    uri = mediaUri.toString(),
                    name = fileName,
                    size = fileSize,
                    isVideo = (isVideo && durationColumn != -1),
                    timestamp = timestamp,
                    duration = duration
                )
            )
        }
    }
    return mediaList
}