package com.valerie.vp_week7.soal2.model

import java.util.Locale

data class Artist(
    val id: String,
    val name: String,
    val genre: String,
    val country: String,
    val biography: String,
    val bannerUrl: String?,
    val thumbUrl: String?,
)

data class Album(
    val id: String,
    val title: String,
    val artistName: String,
    val year: String,
    val genre: String,
    val coverUrl: String?,

    val coverRes: Int? = null,
)

data class AlbumDetail(
    val id: String,
    val title: String,
    val artistName: String,
    val year: String,
    val genre: String,
    val description: String,
    val coverUrl: String?,
    val coverRes: Int? = null,
)

data class Track(
    val id: String,
    val number: Int,
    val title: String,
    val genre: String,
    val durationMs: Long?,
) {

    val durationText: String
        get() = durationMs?.takeIf { it > 0 }?.let {
            val totalSec = it / 1000
            String.format(Locale.US, "%02d:%02d", totalSec / 60, totalSec % 60)
        } ?: "--:--"
}

data class ArtistProfile(
    val artist: Artist,
    val albums: List<Album>,
)

data class AlbumWithTracks(
    val album: AlbumDetail,
    val tracks: List<Track>,
)
