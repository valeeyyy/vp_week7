package com.valerie.vp_week7.soal1.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.valerie.vp_week7.soal1.ui.theme.PokeDivider
import com.valerie.vp_week7.soal1.ui.theme.PokeHeader
import com.valerie.vp_week7.soal1.ui.theme.PokeOutline
import com.valerie.vp_week7.soal1.ui.theme.PokeRed
import com.valerie.vp_week7.soal1.ui.theme.PokeRedSoft
import com.valerie.vp_week7.soal1.ui.theme.PokeSurface
import com.valerie.vp_week7.soal1.ui.theme.PokeSurfaceHigh
import com.valerie.vp_week7.soal1.ui.theme.PokeTextPrimary
import com.valerie.vp_week7.soal1.ui.theme.PokeTextSecondary
import com.valerie.vp_week7.soal1.ui.theme.PokeTextTertiary
import com.valerie.vp_week7.soal1.ui.theme.PriceGreen
import com.valerie.vp_week7.soal1.ui.theme.PriceGreenBg
import com.valerie.vp_week7.soal1.util.UiError

@Composable
fun RemoteImage(
    url: String?,
    @DrawableRes placeholder: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {

    val local = painterResource(placeholder)
    val blank = remember { ColorPainter(PokeSurfaceHigh) }
    val inPreview = LocalInspectionMode.current
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        placeholder = if (inPreview) local else blank,
        error = if (inPreview) local else blank,
        fallback = local,
        contentScale = contentScale,
        modifier = modifier,
    )
}

@Composable
fun PokeballLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(46.dp)
            .background(PokeRed, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.CatchingPokemon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(28.dp),
        )
    }
}

@Composable
fun AppHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(PokeHeader)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PokeballLogo()
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text("PokéTracker", style = MaterialTheme.typography.titleLarge, color = PokeTextPrimary)
            Spacer(Modifier.height(2.dp))
            Text(
                "Real-time TCG Card Valuation",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = PokeTextSecondary,
            )
        }
        LiveBadge()
    }
}

@Composable
private fun LiveBadge() {
    Row(
        modifier = Modifier
            .background(PriceGreenBg, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(6.dp).background(PriceGreen, CircleShape))
        Spacer(Modifier.width(6.dp))
        Text("LIVE API", style = MaterialTheme.typography.labelSmall, color = PriceGreen)
    }
}

@Composable
fun CardSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val shape = RoundedCornerShape(16.dp)

    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .border(1.dp, PokeOutline, shape),
        singleLine = true,
        shape = shape,
        textStyle = MaterialTheme.typography.bodyLarge,
        placeholder = { Text("Search Pokémon cards…", style = MaterialTheme.typography.bodyLarge, color = PokeTextTertiary) },
        leadingIcon = {
            Icon(Icons.Rounded.Search, contentDescription = null, tint = PokeRed, modifier = Modifier.padding(start = 6.dp))
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Rounded.Close, contentDescription = "Clear", tint = PokeTextSecondary)
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            keyboard?.hide()
            focusManager.clearFocus()
            onSearch()
        }),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = PokeSurface,
            unfocusedContainerColor = PokeSurface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = PokeRed,
            focusedTextColor = PokeTextPrimary,
            unfocusedTextColor = PokeTextPrimary,
        ),
    )
}

@Composable
fun SuggestionChips(
    suggestions: List<String>,
    onClick: (String) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(suggestions, key = { it }) { name ->
            Text(
                text = name,
                style = MaterialTheme.typography.labelMedium,
                color = PokeTextPrimary,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(PokeSurface)
                    .border(1.dp, PokeOutline, RoundedCornerShape(50))
                    .clickable { onClick(name) }
                    .padding(horizontal = 18.dp, vertical = 9.dp),
            )
        }
    }
}

@Composable
fun LoadingState(message: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(color = PokeRed, strokeWidth = 3.dp, modifier = Modifier.size(40.dp))
        Spacer(Modifier.height(24.dp))
        Text(message, style = MaterialTheme.typography.bodyMedium, color = PokeTextSecondary, textAlign = TextAlign.Center)
    }
}

@Composable
fun ErrorState(error: UiError, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.size(72.dp).background(PokeSurfaceHigh, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = if (error.isEmpty) Icons.Rounded.SearchOff else Icons.Rounded.WifiOff,
                contentDescription = null,
                tint = PokeRedSoft,
                modifier = Modifier.size(34.dp),
            )
        }
        Spacer(Modifier.height(20.dp))
        Text(
            error.title,
            style = MaterialTheme.typography.titleMedium,
            color = PokeRedSoft,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            error.message,
            style = MaterialTheme.typography.bodyMedium,
            color = PokeTextSecondary,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PokeRed, contentColor = Color.White),
            contentPadding = PaddingValues(horizontal = 30.dp, vertical = 12.dp),
        ) {
            Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Try Again", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }
}

@Composable
fun InfoTable(rows: List<Pair<String, String>>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(PokeSurface),
    ) {
        rows.forEachIndexed { index, (label, value) ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(label, style = MaterialTheme.typography.bodyMedium, color = PokeTextSecondary, modifier = Modifier.weight(1f))
                Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = PokeTextPrimary)
            }
            if (index < rows.lastIndex) {
                HorizontalDivider(color = PokeDivider, modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}
