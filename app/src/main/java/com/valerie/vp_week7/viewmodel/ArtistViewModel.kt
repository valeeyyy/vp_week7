package com.valerie.vp_week7.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.valerie.vp_week7.data.remote.ServiceLocator
import com.valerie.vp_week7.data.repository.MusicRepository
import com.valerie.vp_week7.model.ArtistProfile
import com.valerie.vp_week7.util.toUserMessage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Kondisi konten (di bawah search bar) layar Artist. */
sealed interface ArtistContentState {
    data object Loading : ArtistContentState
    data class Success(val profile: ArtistProfile) : ArtistContentState
    data class Error(val message: String) : ArtistContentState
}

/** Seluruh kondisi layar Artist dalam satu objek. */
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

    /** Dipanggil saat user menekan tombol cari di keyboard. */
    fun search() {
        val name = _uiState.value.query.trim()
        if (name.isEmpty()) return
        lastSearched = name
        load(name)
    }

    /** Tombol "Coba Lagi": ulangi pencarian terakhir. */
    fun retry() = load(lastSearched)

    private fun load(name: String) {
        searchJob?.cancel() // batalkan request lama bila user mencari lagi dengan cepat
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
