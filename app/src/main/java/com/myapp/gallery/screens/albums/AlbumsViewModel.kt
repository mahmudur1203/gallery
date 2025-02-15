package com.myapp.gallery.screens.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.gallery.domain.model.Album
import com.myapp.gallery.domain.state.LayoutOrientation
import com.myapp.gallery.domain.state.Resource
import com.myapp.gallery.domain.state.SortType
import com.myapp.gallery.domain.usecase.GetAlbumsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

val availableSortTypes = listOf(
    SortType.NAME,
    SortType.TIME,
    SortType.COUNT
)

sealed class AlbumsUiState {
    data object Loading : AlbumsUiState()
    data class Success(
        val albums: List<Album>,
        val sortType: SortType = SortType.TIME,
        val layoutOrientation: LayoutOrientation = LayoutOrientation.GRID
    ) : AlbumsUiState()

    data class Error(val message: String) : AlbumsUiState()
}

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val getAlbumsUseCase: GetAlbumsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AlbumsUiState>(AlbumsUiState.Loading)
    val uiState: StateFlow<AlbumsUiState> = _uiState


    init {
        fetchAlbums()
    }

    fun fetchAlbums() {

        _uiState.value = AlbumsUiState.Loading

        viewModelScope.launch {

            getAlbumsUseCase().collect { result ->

                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = AlbumsUiState.Loading
                    }
                    is Resource.Success -> {

                        if(result.data.isNotEmpty()){
                            _uiState.value = AlbumsUiState.Success(
                                albums = sortAlbums(result.data, SortType.TIME),
                                sortType = SortType.TIME,
                                layoutOrientation = LayoutOrientation.GRID
                            )
                        }else{
                            _uiState.value = AlbumsUiState.Error("No Albums Found")
                        }
                    }
                    is Resource.Error -> {
                        _uiState.value = AlbumsUiState.Error(result.message)
                    }
                    else -> {
                    }
                }

            }
        }

    }

    private fun sortAlbums(albums: List<Album>, sortType: SortType): List<Album> {
        return when (sortType) {
            SortType.NAME -> albums.sortedBy { it.name }
            SortType.TIME -> albums.sortedByDescending { it.timestamp }
            SortType.COUNT -> albums.sortedByDescending { it.itemCount }
            else -> {
                albums.sortedBy { it.name }
            }
        }
    }

    fun updateSortType(newSortType: SortType) {
        val currentState = _uiState.value
        if (currentState is AlbumsUiState.Success) {
            _uiState.value = currentState.copy(
                albums = sortAlbums(currentState.albums, newSortType),
                sortType = newSortType
            )
        }
    }

    fun toggleLayoutType() {
        val currentState = _uiState.value
        if (currentState is AlbumsUiState.Success) {
            _uiState.value = currentState.copy(
                layoutOrientation = if (currentState.layoutOrientation == LayoutOrientation.GRID) LayoutOrientation.LIST else LayoutOrientation.GRID
            )
        }
    }
}

