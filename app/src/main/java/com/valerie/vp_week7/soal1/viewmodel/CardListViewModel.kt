package com.valerie.vp_week7.soal1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.valerie.vp_week7.soal1.data.remote.ServiceLocator
import com.valerie.vp_week7.soal1.data.repository.CardRepository
import com.valerie.vp_week7.soal1.model.PokemonCard
import com.valerie.vp_week7.soal1.util.UiError
import com.valerie.vp_week7.soal1.util.toUiError
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface CardsState {
    data object Loading : CardsState
    data class Success(val cards: List<PokemonCard>) : CardsState
    data class Error(val error: UiError) : CardsState
}

data class CardListUiState(
    val query: String = DEFAULT_QUERY,
    val suggestions: List<String> = SUGGESTIONS,
    val cards: CardsState = CardsState.Loading,
)

const val DEFAULT_QUERY = "Charizard"
val SUGGESTIONS = listOf("Charizard", "Pikachu", "Mewtwo", "Gengar", "Eevee", "Snorlax")

class CardListViewModel(
    private val repository: CardRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CardListUiState())
    val uiState: StateFlow<CardListUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null
    private var lastQuery = DEFAULT_QUERY

    init {
        search()
    }

    fun onQueryChange(query: String) = _uiState.update { it.copy(query = query) }

    fun onClearQuery() = _uiState.update { it.copy(query = "") }

    fun search() {
        val q = _uiState.value.query.trim()
        if (q.isEmpty()) return
        load(q, forceRefresh = false)
    }

    fun onSuggestionClick(name: String) {
        _uiState.update { it.copy(query = name) }
        load(name, forceRefresh = false)
    }

    fun retry() = load(lastQuery, forceRefresh = true)

    private fun load(query: String, forceRefresh: Boolean) {
        lastQuery = query
        searchJob?.cancel()
        _uiState.update { it.copy(cards = CardsState.Loading) }
        searchJob = viewModelScope.launch {
            repository.searchCards(query, forceRefresh)
                .onSuccess { cards -> _uiState.update { it.copy(cards = CardsState.Success(cards)) } }
                .onFailure { e -> _uiState.update { it.copy(cards = CardsState.Error(e.toUiError())) } }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { CardListViewModel(ServiceLocator.cardRepository) }
        }
    }
}
