package com.myapp.gallery.ui.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.state.Resource
import com.myapp.gallery.domain.usecase.GetAlbumsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val getAlbumsUseCase: GetAlbumsUseCase
) : ViewModel() {

    private val _albums = MutableStateFlow<Resource<List<Album>>?>(null)
    val albums: StateFlow<Resource<List<Album>>?> = _albums



    fun fetchAlbums() {

        _albums.value = Resource.Loading

        viewModelScope.launch {
            _albums.value = getAlbumsUseCase()
        }

    }
}