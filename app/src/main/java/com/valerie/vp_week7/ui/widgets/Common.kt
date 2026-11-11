package com.valerie.vp_week7.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.valerie.vp_week7.ui.theme.ErrorRed
import com.valerie.vp_week7.ui.theme.GreenAccent
import com.valerie.vp_week7.ui.theme.GreenContainer
import com.valerie.vp_week7.ui.theme.OnGreen
import com.valerie.vp_week7.ui.theme.OutlineDark
import com.valerie.vp_week7.ui.theme.SurfaceDark
import com.valerie.vp_week7.ui.theme.TextPrimary
import com.valerie.vp_week7.ui.theme.TextSecondary
import com.valerie.vp_week7.ui.theme.TextTertiary

/**
 * Gambar dari internet (Coil). Gambar lokal di res/drawable dipakai sebagai
 * placeholder saat loading, saat gagal, dan saat URL kosong -> UI tidak pernah "bolong".
 * Di @Preview, Coil tidak memuat jaringan sehingga drawable lokal ini yang tampil.
 */
@Composable
fun RemoteImage(
    url: String?,
    @DrawableRes placeholder: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val local = painterResource(placeholder)
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        placeholder = local,
        error = local,
        fallback = local,
        contentScale = contentScale,
        modifier = modifier.background(SurfaceDark),
    )
}

/** Header "Music Artist" dengan logo lingkaran hijau. */
@Composable
fun AppHeader(modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(GreenAccent, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.GraphicEq,
                contentDescription = null,
                tint = OnGreen,
                modifier = Modifier.size(24.dp),
            )
        }
        Spacer(Modifier.width(12.dp))
        Text("Music Artist", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
    }
}

/** Search bar berbentuk pill: ikon cari hijau, tombol hapus, submit lewat keyboard. */
@Composable
fun ArtistSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val shape = RoundedCornerShape(28.dp)

    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(1.dp, OutlineDark, shape),
        singleLine = true,
        shape = shape,
        textStyle = MaterialTheme.typography.bodyLarge,
        placeholder = { Text("Cari artis…", style = MaterialTheme.typography.bodyLarge, color = TextTertiary) },
        leadingIcon = {
            Icon(Icons.Rounded.Search, contentDescription = null, tint = GreenAccent, modifier = Modifier.padding(start = 4.dp))
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Rounded.Close, contentDescription = "Hapus", tint = TextSecondary)
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
            focusedContainerColor = SurfaceDark,
            unfocusedContainerColor = SurfaceDark,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = GreenAccent,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
        ),
    )
}

/** Badge pill hijau, mis. "51 Album" / "11 Lagu". */
@Composable
fun CountBadge(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = GreenAccent,
        modifier = modifier
            .background(GreenContainer, RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 5.dp),
    )
}

/** Chip info abu-abu, mis. "Tahun 2002". */
@Composable
fun InfoChip(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = TextSecondary,
        modifier = modifier
            .background(SurfaceDark, RoundedCornerShape(50))
            .padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

/**
 * Paragraf yang bisa dibuka/tutup. Tombol toggle hanya muncul bila teks memang
 * lebih panjang dari [collapsedLines].
 */
@Composable
fun ExpandableText(
    text: String,
    modifier: Modifier = Modifier,
    collapsedLines: Int = 3,
    expandLabel: String = "Baca selengkapnya",
    collapseLabel: String = "Tutup ringkasan",
) {
    var expanded by rememberSaveable(text) { mutableStateOf(false) }
    var overflowing by remember(text) { mutableStateOf(false) }

    Column(modifier.animateContentSize()) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            maxLines = if (expanded) Int.MAX_VALUE else collapsedLines,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { if (!expanded) overflowing = it.hasVisualOverflow },
        )
        if (overflowing || expanded) {
            Spacer(Modifier.height(14.dp))
            Text(
                text = if (expanded) collapseLabel else expandLabel,
                style = MaterialTheme.typography.labelMedium,
                color = GreenAccent,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) { expanded = !expanded },
            )
        }
    }
}

/** Indikator loading di tengah layar dengan keterangan. */
@Composable
fun LoadingState(message: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(
            color = GreenAccent,
            strokeWidth = 3.dp,
            modifier = Modifier.size(40.dp),
        )
        Spacer(Modifier.height(28.dp))
        Text(message, style = MaterialTheme.typography.bodyMedium, color = TextSecondary, textAlign = TextAlign.Center)
    }
}

/** Tampilan error yang konsisten: ikon merah, judul, pesan, tombol "Coba Lagi". */
@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Terjadi Kendala",
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = null,
            tint = ErrorRed,
            modifier = Modifier.size(64.dp),
        )
        Spacer(Modifier.height(20.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
        Spacer(Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(28.dp))
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = GreenAccent, contentColor = OnGreen),
            contentPadding = PaddingValues(horizontal = 28.dp, vertical = 12.dp),
        ) {
            Icon(Icons.Filled.Refresh, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Coba Lagi", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}
