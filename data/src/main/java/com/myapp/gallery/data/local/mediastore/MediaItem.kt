package com.myapp.gallery.data.local.mediastore

import com.myapp.gallery.domain.model.Album

data class MediaItem(
    val name: String,
    val itemCount: Int,
    val thumbnailUri: String?
)

fun MediaItem.toAlbum(): Album {
    return Album(
        name = this.name,
        itemCount = this.itemCount,
        thumbnailUri = this.thumbnailUri,
    )
}