package com.valerie.vp_week7.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.valerie.vp_week7.R
import com.valerie.vp_week7.model.Album
import com.valerie.vp_week7.model.Artist
import com.valerie.vp_week7.model.ArtistProfile
import com.valerie.vp_week7.ui.theme.Background
import com.valerie.vp_week7.ui.theme.GreenAccent
import com.valerie.vp_week7.ui.theme.TextPrimary
import com.valerie.vp_week7.ui.theme.TextSecondary
import com.valerie.vp_week7.ui.theme.Vp_week7Theme
import com.valerie.vp_week7.ui.widgets.AlbumCard
import com.valerie.vp_week7.ui.widgets.AppHeader
import com.valerie.vp_week7.ui.widgets.ArtistSearchBar
import com.valerie.vp_week7.ui.widgets.CountBadge
import com.valerie.vp_week7.ui.widgets.ErrorState
import com.valerie.vp_week7.ui.widgets.ExpandableText
import com.valerie.vp_week7.ui.widgets.LoadingState
import com.valerie.vp_week7.ui.widgets.RemoteImage
import com.valerie.vp_week7.viewmodel.ArtistContentState
import com.valerie.vp_week7.viewmodel.ArtistUiState
import com.valerie.vp_week7.viewmodel.ArtistViewModel

private val ScreenPadding = 20.dp

/** STATEFUL: tahu ViewModel, mengumpulkan state, meneruskan event. */
@Composable
fun ArtistScreen(
    onAlbumClick: (String) -> Unit,
    viewModel: ArtistViewModel = viewModel(factory = ArtistViewModel.Factory),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ArtistContent(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onSearch = viewModel::search,
        onClear = viewModel::onClearQuery,
        onRetry = viewModel::retry,
        onAlbumClick = onAlbumClick,
    )
}

/** STATELESS: hanya menggambar UI dari state -> mudah di-preview & dites. */
@Composable
fun ArtistContent(
    state: ArtistUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    onRetry: () -> Unit,
    onAlbumClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding(),
    ) {
        // ---- Bagian tetap di atas: header + search bar ----
        AppHeader(Modifier.padding(start = ScreenPadding, end = ScreenPadding, top = 24.dp))
        Spacer(Modifier.height(20.dp))
        ArtistSearchBar(
            query = state.query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            onClear = onClear,
            modifier = Modifier.padding(horizontal = ScreenPadding),
        )

        // ---- Konten: loading / error / data ----
        Box(Modifier.fillMaxSize()) {
            when (val content = state.content) {
                ArtistContentState.Loading -> LoadingState(
                    message = "Memuat data artis…",
                    modifier = Modifier.align(Alignment.Center),
                )
                is ArtistContentState.Error -> ErrorState(
                    message = content.message,
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.Center),
                )
                is ArtistContentState.Success -> ArtistBody(content.profile, onAlbumClick)
            }
        }
    }
}

@Composable
private fun ArtistBody(profile: ArtistProfile, onAlbumClick: (String) -> Unit) {
    val navBar = WindowInsets.navigationBars.asPaddingValues()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = ScreenPadding,
            end = ScreenPadding,
            top = 24.dp,
            bottom = 24.dp + navBar.calculateBottomPadding(),
        ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        fullWidth("hero") { ArtistHeroCard(profile.artist) }
        fullWidth("about") { AboutSection(profile.artist.biography) }
        fullWidth("discography-header") { DiscographyHeader(profile.albums.size) }

        if (profile.albums.isEmpty()) {
            fullWidth("no-albums") {
                Text(
                    "Belum ada album untuk artis ini.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(vertical = 24.dp),
                )
            }
        } else {
            items(profile.albums, key = { it.id }) { album ->
                AlbumCard(album = album, onClick = { onAlbumClick(album.id) })
            }
        }
    }
}

/** Item grid yang memakan seluruh lebar (2 kolom). */
private fun LazyGridScope.fullWidth(key: String, content: @Composable () -> Unit) {
    item(key = key, span = { GridItemSpan(maxLineSpan) }) { content() }
}

@Composable
private fun ArtistHeroCard(artist: Artist) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .clip(RoundedCornerShape(22.dp)),
    ) {
        RemoteImage(
            url = artist.bannerUrl,
            placeholder = R.drawable.artist_banner,
            contentDescription = artist.name,
            modifier = Modifier.fillMaxSize(),
        )
        // Gradasi gelap supaya teks di bawah tetap terbaca
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.0f to Color.Transparent,
                        0.45f to Color.Transparent,
                        1.0f to Background.copy(alpha = 0.95f),
                    )
                )
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 22.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RemoteImage(
                url = artist.thumbUrl,
                placeholder = R.drawable.artist_thumb,
                contentDescription = null,
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape),
            )
            Spacer(Modifier.width(14.dp))
            Column {
                Text(
                    text = artist.name,
                    style = MaterialTheme.typography.displaySmall,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(artist.genre, style = MaterialTheme.typography.labelSmall, color = GreenAccent)
                    if (artist.country.isNotEmpty()) {
                        Box(
                            Modifier
                                .padding(horizontal = 8.dp)
                                .size(3.dp)
                                .background(TextSecondary, CircleShape)
                        )
                        Text(
                            artist.country,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextPrimary.copy(alpha = 0.85f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AboutSection(biography: String) {
    Column(Modifier.fillMaxWidth().padding(top = 8.dp)) {
        Text("Tentang Artis", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
        Spacer(Modifier.height(8.dp))
        ExpandableText(
            text = biography.ifEmpty { "Biografi belum tersedia." },
            collapsedLines = 3,
        )
    }
}

@Composable
private fun DiscographyHeader(count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 28.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text("Diskografi Album", style = MaterialTheme.typography.titleLarge, color = TextPrimary)
            Text("Koleksi rilisan resmi", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        CountBadge("$count Album")
    }
}

// ------------------------------------------------------------------ Preview

private val previewProfile = ArtistProfile(
    artist = Artist(
        id = "111239",
        name = "Coldplay",
        genre = "Alternative Rock",
        country = "London, England",
        biography = "Coldplay are a British rock band formed in London in 1997. They consist of vocalist " +
            "and pianist Chris Martin, guitarist Jonny Buckland, bassist Guy Berryman, drummer Will Champion " +
            "and creative director Phil Harvey.",
        bannerUrl = null,
        thumbUrl = null,
    ),
    albums = listOf(
        Album("2109614", "A Rush of Blood to the Head", "Coldplay", "2002", "Pop-Rock", null),
        Album("2109615", "Parachutes", "Coldplay", "2000", "Pop-Rock", null),
    ),
)

@Preview(showBackground = true, backgroundColor = 0xFF121417, heightDp = 900)
@Composable
private fun ArtistContentPreview() {
    Vp_week7Theme {
        ArtistContent(
            state = ArtistUiState(query = "Coldplay", content = ArtistContentState.Success(previewProfile)),
            onQueryChange = {}, onSearch = {}, onClear = {}, onRetry = {}, onAlbumClick = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121417)
@Composable
private fun ArtistErrorPreview() {
    Vp_week7Theme {
        ArtistContent(
            state = ArtistUiState(
                query = "Coldplay",
                content = ArtistContentState.Error("Unable to resolve host \"www.theaudiodb.com\": No address associated with hostname"),
            ),
            onQueryChange = {}, onSearch = {}, onClear = {}, onRetry = {}, onAlbumClick = {},
        )
    }
}
