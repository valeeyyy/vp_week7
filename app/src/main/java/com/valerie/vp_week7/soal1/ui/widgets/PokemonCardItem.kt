package com.valerie.vp_week7.soal1.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valerie.vp_week7.R
import com.valerie.vp_week7.soal1.model.PokemonCard
import com.valerie.vp_week7.soal1.model.toUsd
import com.valerie.vp_week7.soal1.ui.theme.PokeOutline
import com.valerie.vp_week7.soal1.ui.theme.PokeSurface
import com.valerie.vp_week7.soal1.ui.theme.PokeSurfaceHigh
import com.valerie.vp_week7.soal1.ui.theme.PokeTextPrimary
import com.valerie.vp_week7.soal1.ui.theme.PokeTextSecondary
import com.valerie.vp_week7.soal1.ui.theme.PokeTrackerTheme
import com.valerie.vp_week7.soal1.ui.theme.PriceGreen
import com.valerie.vp_week7.soal1.ui.theme.PriceGreenBg

const val CARD_ASPECT_RATIO = 63f / 88f

@Composable
fun PokemonCardItem(
    card: PokemonCard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .background(PokeSurface)
            .border(1.dp, PokeOutline, shape)
            .clickable(onClick = onClick)
            .padding(12.dp),
    ) {
        Box {
            RemoteImage(
                url = card.imageUrl,
                placeholder = R.drawable.card_charizard_xy29,
                contentDescription = card.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(CARD_ASPECT_RATIO)
                    .clip(RoundedCornerShape(8.dp)),
            )
            card.hp?.let { hp ->
                Text(
                    text = "$hp HP",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .background(Color(0xE6161B22), RoundedCornerShape(6.dp))
                        .padding(horizontal = 7.dp, vertical = 3.dp),
                )
            }
        }
        Spacer(Modifier.height(14.dp))
        Text(
            text = card.name,
            style = MaterialTheme.typography.titleSmall,
            color = PokeTextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(3.dp))
        Text(
            text = "${card.setName} • #${card.cardNumber}",
            style = MaterialTheme.typography.bodySmall,
            color = PokeTextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            PriceTag(card.marketPrice.toUsd())
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.width(6.dp))
            Text(
                text = card.rarity,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 11.sp,
                color = PokeTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .background(PokeSurfaceHigh, RoundedCornerShape(6.dp))
                    .padding(horizontal = 7.dp, vertical = 3.dp),
            )
        }
    }
}

@Composable
private fun PriceTag(price: String) {
    Row(
        modifier = Modifier
            .background(PriceGreenBg, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.AutoMirrored.Rounded.TrendingUp,
            contentDescription = null,
            tint = PriceGreen,
            modifier = Modifier.size(14.dp),
        )
        Spacer(Modifier.width(4.dp))
        Text(price, color = PriceGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121821, widthDp = 200)
@Composable
private fun PokemonCardItemPreview() {
    PokeTrackerTheme {
        PokemonCardItem(
            card = PokemonCard(
                id = "1", tcgPlayerId = "96392", name = "Charizard EX - XY29", setName = "XY Promos",
                cardNumber = "XY29", rarity = "Promo", hp = "180", type = "Fire", artist = "Eske Yoshinob",
                imageUrl = null, imageLargeUrl = null, marketPrice = 21.78, lowPrice = 2.84,
                listings = 109, sellers = 88, primaryPrinting = "Holofoil",
            ),
            onClick = {},
            modifier = Modifier.padding(8.dp),
        )
    }
}
