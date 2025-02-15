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

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val getAlbumsUseCase: GetAlbumsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AlbumsUiState>(AlbumsUiState.Nothing)
    val uiState: StateFlow<AlbumsUiState> = _uiState



    init {
        fetchAlbums()
    }

    fun fetchAlbums() {

        if(_uiState.value == AlbumsUiState.Nothing){
            _uiState.value = AlbumsUiState.Loading
        }

        viewModelScope.launch {

            getAlbumsUseCase().collect { it ->

                when(it){
                    is Resource.Loading -> {
                        _uiState.value = AlbumsUiState.Loading
                    }
                    is Resource.Success -> {
                        _uiState.value = AlbumsUiState.Success(
                            albums = sortAlbums(it.data, SortType.TIME),
                            sortType = SortType.TIME,
                            layoutOrientation = LayoutOrientation.GRID
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = AlbumsUiState.Error(it.message)
                    }
                    else -> {
                        _uiState.value = AlbumsUiState.Nothing
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

sealed class AlbumsUiState {
    data object Loading : AlbumsUiState()
    data class Success(
        val albums: List<Album>,
        val sortType: SortType = SortType.TIME,
        val layoutOrientation: LayoutOrientation = LayoutOrientation.GRID
    ) : AlbumsUiState()
    data class Error(val message: String) : AlbumsUiState()
    data object Nothing : AlbumsUiState()
}