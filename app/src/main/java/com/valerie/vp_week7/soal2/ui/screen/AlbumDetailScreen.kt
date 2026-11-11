package com.valerie.vp_week7.soal2.ui.screen

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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.valerie.vp_week7.R
import com.valerie.vp_week7.soal2.model.AlbumDetail
import com.valerie.vp_week7.soal2.model.AlbumWithTracks
import com.valerie.vp_week7.soal2.model.Track
import com.valerie.vp_week7.soal2.ui.theme.Background
import com.valerie.vp_week7.soal2.ui.theme.GreenAccent
import com.valerie.vp_week7.soal2.ui.theme.TextPrimary
import com.valerie.vp_week7.soal2.ui.theme.TextSecondary
import com.valerie.vp_week7.soal2.ui.theme.Vp_week7Theme
import com.valerie.vp_week7.soal2.ui.widgets.CountBadge
import com.valerie.vp_week7.soal2.ui.widgets.ErrorState
import com.valerie.vp_week7.soal2.ui.widgets.ExpandableText
import com.valerie.vp_week7.soal2.ui.widgets.InfoChip
import com.valerie.vp_week7.soal2.ui.widgets.LoadingState
import com.valerie.vp_week7.soal2.ui.widgets.RemoteImage
import com.valerie.vp_week7.soal2.ui.widgets.TrackRow
import com.valerie.vp_week7.soal2.viewmodel.AlbumDetailUiState
import com.valerie.vp_week7.soal2.viewmodel.AlbumDetailViewModel

private val ScreenPadding = 20.dp

@Composable
fun AlbumDetailScreen(
    onBackClick: () -> Unit,
    viewModel: AlbumDetailViewModel = viewModel(factory = AlbumDetailViewModel.Factory),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    AlbumDetailContent(state = state, onBackClick = onBackClick, onRetry = viewModel::load)
}

@Composable
fun AlbumDetailContent(
    state: AlbumDetailUiState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding(),
    ) {
        DetailTopBar(onBackClick)

        Box(Modifier.fillMaxSize()) {
            when (state) {
                AlbumDetailUiState.Loading -> LoadingState(
                    message = "Memuat detail album & track list…",
                    modifier = Modifier.align(Alignment.Center),
                )
                is AlbumDetailUiState.Error -> ErrorState(
                    message = state.message,
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.Center),
                )
                is AlbumDetailUiState.Success -> AlbumDetailBody(state.data)
            }
        }
    }
}

@Composable
private fun DetailTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = ScreenPadding, top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = TextPrimary)
        }
        Spacer(Modifier.width(8.dp))
        Text("Detail Album", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
    }
}

@Composable
private fun AlbumDetailBody(data: AlbumWithTracks) {
    val album = data.album
    val navBar = WindowInsets.navigationBars.asPaddingValues()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = ScreenPadding,
            end = ScreenPadding,
            top = 16.dp,
            bottom = 24.dp + navBar.calculateBottomPadding(),
        ),
    ) {
        item(key = "header") { AlbumHeader(album) }
        item(key = "description") { DescriptionSection(album.description) }
        item(key = "tracks-header") { TracklistHeader(data.tracks.size) }

        if (data.tracks.isEmpty()) {
            item(key = "no-tracks") {
                Text(
                    "Daftar lagu belum tersedia untuk album ini.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(vertical = 16.dp),
                )
            }
        } else {
            items(data.tracks, key = { it.id }) { track -> TrackRow(track) }
        }
    }
}

@Composable
private fun AlbumHeader(album: AlbumDetail) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        RemoteImage(
            url = album.coverUrl,
            placeholder = album.coverRes ?: R.drawable.album_cover,
            contentDescription = album.title,
            modifier = Modifier
                .size(240.dp)
                .shadow(
                    elevation = 28.dp,
                    shape = RoundedCornerShape(18.dp),
                    ambientColor = GreenAccent.copy(alpha = 0.35f),
                    spotColor = GreenAccent.copy(alpha = 0.35f),
                )
                .clip(RoundedCornerShape(18.dp)),
        )
        Spacer(Modifier.height(28.dp))
        Text(
            text = album.title,
            style = MaterialTheme.typography.headlineSmall,
            color = TextPrimary,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = album.artistName,
            color = GreenAccent,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            if (album.year.isNotEmpty()) InfoChip("Tahun ${album.year}")
            InfoChip(album.genre)
        }
    }
}

@Composable
private fun DescriptionSection(description: String) {
    Column(Modifier.fillMaxWidth().padding(top = 28.dp)) {
        Text("Deskripsi Album", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
        Spacer(Modifier.height(8.dp))
        ExpandableText(
            text = description.ifEmpty { "Deskripsi belum tersedia." },
            collapsedLines = 3,
        )
    }
}

@Composable
private fun TracklistHeader(count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 36.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.MusicNote,
            contentDescription = null,
            tint = GreenAccent,
            modifier = Modifier.size(24.dp),
        )
        Spacer(Modifier.width(10.dp))
        Text(
            "Daftar Lagu",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            modifier = Modifier.weight(1f),
        )
        CountBadge("$count Lagu")
    }
}

private val previewAlbum = AlbumWithTracks(
    album = AlbumDetail(
        id = "2109614",
        title = "A Rush of Blood to the Head",
        artistName = "Coldplay",
        year = "2002",
        genre = "Pop-Rock",
        description = "A Rush of Blood to the Head is the second studio album by British alternative rock band " +
            "Coldplay. Released on 26 August 2002 in the UK through the label Parlophone, the album was " +
            "produced by the band and British record producer Ken Nelson.",
        coverUrl = null,
    ),
    tracks = listOf(
        Track("1", 1, "Politik", "Pop-Rock", 318_626),
        Track("2", 2, "In My Place", "Pop-Rock", 228_000),
        Track("3", 3, "God Put a Smile upon Your Face", "Pop-Rock", 297_000),
    ),
)

@Preview(showBackground = true, backgroundColor = 0xFF121417, heightDp = 1000)
@Composable
private fun AlbumDetailPreview() {
    Vp_week7Theme {
        AlbumDetailContent(state = AlbumDetailUiState.Success(previewAlbum), onBackClick = {}, onRetry = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121417)
@Composable
private fun AlbumDetailLoadingPreview() {
    Vp_week7Theme {
        AlbumDetailContent(state = AlbumDetailUiState.Loading, onBackClick = {}, onRetry = {})
    }
}
