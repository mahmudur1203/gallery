package com.myapp.gallery.data.local.mediastore.model

import com.myapp.gallery.domain.model.Media

data class MediaFile (
    val id : Long,
    val uri: String,
    val name: String,
    val size: Long,   // File size in bytes
    val timestamp: Long? = null,
    val isVideo: Boolean = false,
    val duration: Long? = null // Video duration in milliseconds
)

fun MediaFile.isImage() = !isVideo
fun MediaFile.isVideo() = isVideo

fun MediaFile.toMedia() = Media(
    id = id,
    uri = uri,
    name = name,
    size = size,
    timestamp = timestamp,
    isVideo = isVideo,
    duration = duration
)


