package com.valerie.vp_week7.soal1.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Sell
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.valerie.vp_week7.R
import com.valerie.vp_week7.soal1.model.PokemonCard
import com.valerie.vp_week7.soal1.model.toUsd
import com.valerie.vp_week7.soal1.ui.theme.PokeBackground
import com.valerie.vp_week7.soal1.ui.theme.PokeMaroon
import com.valerie.vp_week7.soal1.ui.theme.PokeRed
import com.valerie.vp_week7.soal1.ui.theme.PokeTextPrimary
import com.valerie.vp_week7.soal1.ui.theme.PokeTextSecondary
import com.valerie.vp_week7.soal1.ui.theme.PokeTrackerTheme
import com.valerie.vp_week7.soal1.ui.theme.PriceGreen
import com.valerie.vp_week7.soal1.ui.theme.PriceGreenBg
import com.valerie.vp_week7.soal1.ui.theme.PriceGreenBorder
import com.valerie.vp_week7.soal1.ui.theme.PriceGreenCard
import com.valerie.vp_week7.soal1.ui.theme.PriceYellow
import com.valerie.vp_week7.soal1.ui.theme.PriceYellowBg
import com.valerie.vp_week7.soal1.ui.theme.PriceYellowBorder
import com.valerie.vp_week7.soal1.ui.theme.PriceYellowCard
import com.valerie.vp_week7.soal1.ui.widgets.CARD_ASPECT_RATIO
import com.valerie.vp_week7.soal1.ui.widgets.ErrorState
import com.valerie.vp_week7.soal1.ui.widgets.InfoTable
import com.valerie.vp_week7.soal1.ui.widgets.LoadingState
import com.valerie.vp_week7.soal1.ui.widgets.RemoteImage
import com.valerie.vp_week7.soal1.viewmodel.CardDetailUiState
import com.valerie.vp_week7.soal1.viewmodel.CardDetailViewModel

private val ScreenPadding = 20.dp

@Composable
fun CardDetailScreen(
    onClose: () -> Unit,
    viewModel: CardDetailViewModel = viewModel(factory = CardDetailViewModel.Factory),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    CardDetailContent(state = state, onClose = onClose, onRetry = viewModel::load)
}

@Composable
fun CardDetailContent(
    state: CardDetailUiState,
    onClose: () -> Unit,
    onRetry: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PokeBackground)
            .statusBarsPadding()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(
                Brush.verticalGradient(
                    0f to PokeMaroon,
                    0.42f to PokeBackground,
                    1f to PokeBackground,
                )
            ),
    ) {
        when (state) {
            is CardDetailUiState.Success -> DetailBody(state.card, onClose)
            else -> Box(Modifier.fillMaxSize().padding(top = 60.dp), contentAlignment = Alignment.Center) {
                when (state) {
                    is CardDetailUiState.Error -> ErrorState(state.error, onRetry)
                    else -> LoadingState("Loading card details…")
                }
            }
        }

        Box(
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = 12.dp)
                .size(width = 40.dp, height = 4.dp)
                .background(Color.White.copy(alpha = 0.25f), RoundedCornerShape(50))
        )
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 20.dp, end = 16.dp)
                .size(40.dp),
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Black.copy(alpha = 0.35f)),
        ) {
            Icon(Icons.Rounded.Close, contentDescription = "Close", tint = PokeTextPrimary, modifier = Modifier.size(22.dp))
        }
    }
}

@Composable
private fun DetailBody(card: PokemonCard, onClose: () -> Unit) {
    val navBarBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = ScreenPadding, end = ScreenPadding, top = 36.dp, bottom = 24.dp + navBarBottom),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        RemoteImage(
            url = card.imageLargeUrl,
            placeholder = R.drawable.card_charizard_xy29,
            contentDescription = card.name,
            modifier = Modifier
                .width(225.dp)
                .aspectRatio(CARD_ASPECT_RATIO)
                .shadow(24.dp, RoundedCornerShape(12.dp), spotColor = PokeRed.copy(alpha = 0.5f))
                .clip(RoundedCornerShape(12.dp)),
        )
        Spacer(Modifier.height(22.dp))
        Text(
            card.name,
            style = MaterialTheme.typography.headlineSmall,
            color = PokeTextPrimary,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(4.dp))
        Text(card.setName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, color = PokeTextSecondary)

        Spacer(Modifier.height(22.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            PriceBox(
                label = "Market Price",
                value = card.marketPrice.toUsd(),
                icon = Icons.AutoMirrored.Rounded.TrendingUp,
                accent = PriceGreen,
                iconBg = PriceGreenBg,
                container = PriceGreenCard,
                border = PriceGreenBorder,
                modifier = Modifier.weight(1f),
            )
            PriceBox(
                label = "Lowest Price",
                value = card.lowPrice.toUsd(),
                icon = Icons.Rounded.Sell,
                accent = PriceYellow,
                iconBg = PriceYellowBg,
                container = PriceYellowCard,
                border = PriceYellowBorder,
                modifier = Modifier.weight(1f),
            )
        }

        SectionTitle("Market Activity")
        InfoTable(
            listOf(
                "Active Listings" to (card.listings?.toString() ?: "—"),
                "Verified Sellers" to (card.sellers?.toString() ?: "—"),
                "Primary Printing" to card.primaryPrinting,
            )
        )

        SectionTitle("Card Specifications")
        InfoTable(
            listOf(
                "Card Number" to card.cardNumber,
                "Rarity" to card.rarity,
                "Type" to card.type,
                "Illustrator" to card.artist,
            )
        )

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PokeRed, contentColor = Color.White),
            contentPadding = PaddingValues(vertical = 14.dp),
        ) {
            Text("Done", fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleMedium,
        color = PokeTextPrimary,
        modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 12.dp),
    )
}

@Composable
private fun PriceBox(
    label: String,
    value: String,
    icon: ImageVector,
    accent: Color,
    iconBg: Color,
    container: Color,
    border: Color,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .background(container)
            .border(1.dp, border, shape)
            .padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(30.dp).background(iconBg, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(17.dp))
            }
            Spacer(Modifier.width(10.dp))
            Text(label, style = MaterialTheme.typography.bodySmall, color = PokeTextSecondary)
        }
        Spacer(Modifier.height(12.dp))
        Text(value, color = accent, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121821, heightDp = 1100)
@Composable
private fun CardDetailPreview() {
    PokeTrackerTheme {
        CardDetailContent(
            state = CardDetailUiState.Success(
                PokemonCard(
                    "1", "96392", "Charizard EX - XY29", "XY Promos", "XY29", "Promo", "180", "Fire",
                    "Eske Yoshinob", null, null, 21.78, 2.84, 109, 88, "Holofoil",
                )
            ),
            onClose = {}, onRetry = {},
        )
    }
}
