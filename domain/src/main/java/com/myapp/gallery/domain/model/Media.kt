package com.myapp.gallery.domain.model

import com.sun.jndi.toolkit.url.Uri

data class Media (
    val id : Long,
    val uri: String,
    val name: String,
    val size: Long,   // File size in bytes
    val timestamp: Long? = null,
    val isVideo: Boolean = false,
    val duration: Long? = null // Video duration in milliseconds
)