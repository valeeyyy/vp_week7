package com.valerie.vp_week7.soal2.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.valerie.vp_week7.soal2.data.remote.ServiceLocator
import com.valerie.vp_week7.soal2.data.repository.MusicRepository
import com.valerie.vp_week7.soal2.model.AlbumWithTracks
import com.valerie.vp_week7.soal2.navigation.AlbumDetailRoute
import com.valerie.vp_week7.soal2.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AlbumDetailUiState {
    data object Loading : AlbumDetailUiState
    data class Success(val data: AlbumWithTracks) : AlbumDetailUiState
    data class Error(val message: String) : AlbumDetailUiState
}

class AlbumDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: MusicRepository,
) : ViewModel() {

    private val albumId: String = savedStateHandle.toRoute<AlbumDetailRoute>().albumId

    private val _uiState = MutableStateFlow<AlbumDetailUiState>(AlbumDetailUiState.Loading)
    val uiState: StateFlow<AlbumDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.value = AlbumDetailUiState.Loading
        viewModelScope.launch {
            repository.getAlbumWithTracks(albumId)
                .onSuccess { _uiState.value = AlbumDetailUiState.Success(it) }
                .onFailure { _uiState.value = AlbumDetailUiState.Error(it.toUserMessage()) }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                AlbumDetailViewModel(createSavedStateHandle(), ServiceLocator.musicRepository)
            }
        }
    }
}
