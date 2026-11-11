package com.valerie.vp_week7.soal1.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.valerie.vp_week7.soal1.model.PokemonCard
import com.valerie.vp_week7.soal1.ui.theme.PokeBackground
import com.valerie.vp_week7.soal1.ui.theme.PokeTextPrimary
import com.valerie.vp_week7.soal1.ui.theme.PokeTextSecondary
import com.valerie.vp_week7.soal1.ui.theme.PokeTrackerTheme
import com.valerie.vp_week7.soal1.ui.widgets.AppHeader
import com.valerie.vp_week7.soal1.ui.widgets.CardSearchBar
import com.valerie.vp_week7.soal1.ui.widgets.ErrorState
import com.valerie.vp_week7.soal1.ui.widgets.LoadingState
import com.valerie.vp_week7.soal1.ui.widgets.PokemonCardItem
import com.valerie.vp_week7.soal1.ui.widgets.SuggestionChips
import com.valerie.vp_week7.soal1.util.UiError
import com.valerie.vp_week7.soal1.viewmodel.CardListUiState
import com.valerie.vp_week7.soal1.viewmodel.CardListViewModel
import com.valerie.vp_week7.soal1.viewmodel.CardsState

private val ScreenPadding = 20.dp

@Composable
fun CardListScreen(
    onCardClick: (PokemonCard) -> Unit,
    viewModel: CardListViewModel = viewModel(factory = CardListViewModel.Factory),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CardListContent(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onSearch = viewModel::search,
        onClear = viewModel::onClearQuery,
        onSuggestionClick = viewModel::onSuggestionClick,
        onRetry = viewModel::retry,
        onCardClick = onCardClick,
    )
}

@Composable
fun CardListContent(
    state: CardListUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    onSuggestionClick: (String) -> Unit,
    onRetry: () -> Unit,
    onCardClick: (PokemonCard) -> Unit,
) {
    val navBarBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val topSection: @Composable () -> Unit = {
        Column {
            AppHeader()
            Spacer(Modifier.height(18.dp))
            CardSearchBar(
                query = state.query,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                onClear = onClear,
                modifier = Modifier.padding(horizontal = ScreenPadding),
            )
            Spacer(Modifier.height(14.dp))
            SuggestionChips(
                suggestions = state.suggestions,
                onClick = onSuggestionClick,
                contentPadding = PaddingValues(horizontal = ScreenPadding),
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PokeBackground)
            .statusBarsPadding(),
    ) {
        when (val cards = state.cards) {
            is CardsState.Success -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp + navBarBottom),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                fullWidth("top") { topSection() }
                fullWidth("results-header") { ResultsHeader(cards.cards.size) }
                itemsIndexed(cards.cards, key = { _, card -> card.id }) { index, card ->

                    val isLeft = index % 2 == 0

                    PokemonCardItem(
                        card = card,
                        onClick = { onCardClick(card) },
                        modifier = Modifier.padding(
                            start = if (isLeft) ScreenPadding else 8.dp,
                            end = if (isLeft) 8.dp else ScreenPadding,
                        ),
                    )
                }
            }

            else -> Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = navBarBottom),
            ) {
                topSection()
                Box(
                    modifier = Modifier.fillMaxWidth().heightIn(min = 460.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    when (cards) {
                        is CardsState.Error -> ErrorState(cards.error, onRetry)
                        else -> LoadingState("Fetching market prices…")
                    }
                }
            }
        }
    }
}

private fun LazyGridScope.fullWidth(key: String, content: @Composable () -> Unit) {
    item(key = key, span = { GridItemSpan(maxLineSpan) }) { content() }
}

@Composable
private fun ResultsHeader(count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = ScreenPadding, end = ScreenPadding, top = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            "Market Results ($count)",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = PokeTextPrimary,
            modifier = Modifier.weight(1f),
        )
        Text("USD • Near Mint", style = MaterialTheme.typography.bodySmall, color = PokeTextSecondary)
    }
}

private val previewCards = listOf(
    PokemonCard("1", "96392", "Charizard EX - XY29", "XY Promos", "XY29", "Promo", "180", "Fire", "Eske Yoshinob", null, null, 21.78, 2.84, 109, 88, "Holofoil"),
    PokemonCard("2", "96379", "Charizard EX - XY17", "XY Promos", "XY17", "Promo", "180", "Fire", "5ban Graphics", null, null, 37.44, 4.68, 60, 41, "Holofoil"),
)

@Preview(showBackground = true, backgroundColor = 0xFF121821, heightDp = 900)
@Composable
private fun CardListPreview() {
    PokeTrackerTheme {
        CardListContent(
            state = CardListUiState(cards = CardsState.Success(previewCards)),
            onQueryChange = {}, onSearch = {}, onClear = {}, onSuggestionClick = {}, onRetry = {}, onCardClick = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121821)
@Composable
private fun CardListErrorPreview() {
    PokeTrackerTheme {
        CardListContent(
            state = CardListUiState(cards = CardsState.Error(UiError("Connection Failed", "HTTP 401"))),
            onQueryChange = {}, onSearch = {}, onClear = {}, onSuggestionClick = {}, onRetry = {}, onCardClick = {},
        )
    }
}
