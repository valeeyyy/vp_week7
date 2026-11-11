package com.valerie.vp_week7.model

import java.util.Locale

/** Model artis yang dipakai UI. */
data class Artist(
    val id: String,
    val name: String,
    val genre: String,
    val country: String,
    val biography: String,
    val bannerUrl: String?,
    val thumbUrl: String?,
)

/** Model album untuk grid diskografi. */
data class Album(
    val id: String,
    val title: String,
    val artistName: String,
    val year: String,
    val genre: String,
    val coverUrl: String?,
)

/** Model album untuk halaman detail. */
data class AlbumDetail(
    val id: String,
    val title: String,
    val artistName: String,
    val year: String,
    val genre: String,
    val description: String,
    val coverUrl: String?,
)

/** Satu lagu pada tracklist album. */
data class Track(
    val id: String,
    val number: Int,
    val title: String,
    val genre: String,
    val durationMs: Long?,
) {
    /** Durasi dalam format mm:ss, mis. "05:18". */
    val durationText: String
        get() = durationMs?.takeIf { it > 0 }?.let {
            val totalSec = it / 1000
            String.format(Locale.US, "%02d:%02d", totalSec / 60, totalSec % 60)
        } ?: "--:--"
}

/** Gabungan data untuk layar Artist: info artis + diskografinya. */
data class ArtistProfile(
    val artist: Artist,
    val albums: List<Album>,
)

/** Gabungan data untuk layar Detail Album. */
data class AlbumWithTracks(
    val album: AlbumDetail,
    val tracks: List<Track>,
)
