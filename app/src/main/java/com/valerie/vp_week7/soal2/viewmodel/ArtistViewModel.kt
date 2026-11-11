package com.valerie.vp_week7.soal2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.valerie.vp_week7.soal2.data.remote.ServiceLocator
import com.valerie.vp_week7.soal2.data.repository.MusicRepository
import com.valerie.vp_week7.soal2.model.ArtistProfile
import com.valerie.vp_week7.soal2.util.toUserMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface ArtistContentState {
    data object Loading : ArtistContentState
    data class Success(val profile: ArtistProfile) : ArtistContentState
    data class Error(val message: String) : ArtistContentState
}

data class ArtistUiState(
    val query: String = DEFAULT_ARTIST,
    val content: ArtistContentState = ArtistContentState.Loading,
)

const val DEFAULT_ARTIST = "Coldplay"

class ArtistViewModel(
    private val repository: MusicRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArtistUiState())
    val uiState: StateFlow<ArtistUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private var lastSearched: String = DEFAULT_ARTIST

    init {
        search()
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun onClearQuery() {
        _uiState.update { it.copy(query = "") }
    }

    fun search() {
        val name = _uiState.value.query.trim()
        if (name.isEmpty()) return
        lastSearched = name
        load(name)
    }

    fun retry() = load(lastSearched)

    private fun load(name: String) {
        searchJob?.cancel()
        _uiState.update { it.copy(content = ArtistContentState.Loading) }
        searchJob = viewModelScope.launch {
            repository.getArtistProfile(name)
                .onSuccess { profile -> _uiState.update { it.copy(content = ArtistContentState.Success(profile)) } }
                .onFailure { e -> _uiState.update { it.copy(content = ArtistContentState.Error(e.toUserMessage())) } }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { ArtistViewModel(ServiceLocator.musicRepository) }
        }
    }
}
