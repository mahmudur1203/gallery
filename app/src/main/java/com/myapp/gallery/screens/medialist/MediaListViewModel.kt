package com.myapp.gallery.screens.medialist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.model.Media
import com.myapp.gallery.domain.state.Resource
import com.myapp.gallery.domain.usecase.GetMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaListViewModel @Inject constructor(private val getMediaUseCase: GetMediaUseCase) : ViewModel() {

    private val _medias = MutableStateFlow<Resource<List<Media>>>(Resource.Empty)
    val medias: StateFlow<Resource<List<Media>>> = _medias


    fun fetchMedias(albumId: Long) {

        if(_medias.value == Resource.Empty){
            _medias.value = Resource.Loading
        }

        viewModelScope.launch {

            getMediaUseCase(albumId).collect {
                _medias.value = it
            }

        }
    }


}