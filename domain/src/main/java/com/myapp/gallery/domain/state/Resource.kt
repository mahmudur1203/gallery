package com.myapp.gallery.domain.state

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()

    val isLoading: Boolean
        get() = this is Loading

    val isSuccess: Boolean
        get() = this is Success<*>

    val isError: Boolean
        get() = this is Error
}