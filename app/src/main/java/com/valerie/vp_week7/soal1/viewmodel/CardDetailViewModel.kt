package com.valerie.vp_week7.soal1.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.valerie.vp_week7.soal1.data.remote.ServiceLocator
import com.valerie.vp_week7.soal1.data.repository.CardRepository
import com.valerie.vp_week7.soal1.model.PokemonCard
import com.valerie.vp_week7.soal1.navigation.CardDetailRoute
import com.valerie.vp_week7.soal1.util.UiError
import com.valerie.vp_week7.soal1.util.toUiError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CardDetailUiState {
    data object Loading : CardDetailUiState
    data class Success(val card: PokemonCard) : CardDetailUiState
    data class Error(val error: UiError) : CardDetailUiState
}

class CardDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: CardRepository,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<CardDetailRoute>()

    private val _uiState = MutableStateFlow<CardDetailUiState>(CardDetailUiState.Loading)
    val uiState: StateFlow<CardDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.value = CardDetailUiState.Loading
        viewModelScope.launch {
            repository.getCard(route.cardId, route.tcgPlayerId)
                .onSuccess { _uiState.value = CardDetailUiState.Success(it) }
                .onFailure { _uiState.value = CardDetailUiState.Error(it.toUiError()) }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                CardDetailViewModel(createSavedStateHandle(), ServiceLocator.cardRepository)
            }
        }
    }
}
