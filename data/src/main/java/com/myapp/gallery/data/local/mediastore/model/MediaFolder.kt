package com.myapp.gallery.data.local.mediastore.model

import com.myapp.gallery.domain.model.Album

data class MediaFolder(
    val id: Long,
    val name: String,
    val itemCount: Int,
    val thumbnailUri: String?
)

fun MediaFolder.toAlbum(): Album {
    return Album(
        id = this.id,
        name = this.name,
        itemCount = this.itemCount,
        thumbnailUri = this.thumbnailUri,
    )
}