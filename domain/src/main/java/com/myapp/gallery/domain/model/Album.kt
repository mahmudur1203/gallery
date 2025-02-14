package com.myapp.gallery.domain.model

data class Album(
    val id: Long,
    val name: String,
    val itemCount: Int,
    val thumbnailUri: String?,
)
