package com.valerie.vp_week7.soal2.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArtistResponseDto(
    val artists: List<ArtistDto>? = null,
)

@Serializable
data class ArtistDto(
    val idArtist: String,
    val strArtist: String,
    val strGenre: String? = null,
    val strStyle: String? = null,
    val strCountry: String? = null,
    val strBiography: String? = null,
    val strBiographyEN: String? = null,
    val strArtistBanner: String? = null,
    val strArtistFanart: String? = null,
    val strArtistWideThumb: String? = null,
    val strArtistThumb: String? = null,
)

@Serializable
data class AlbumResponseDto(
    val album: List<AlbumDto>? = null,
)

@Serializable
data class AlbumDto(
    val idAlbum: String,
    val strAlbum: String,
    val strArtist: String? = null,
    val intYearReleased: String? = null,
    val strGenre: String? = null,
    val strStyle: String? = null,
    val strDescription: String? = null,
    val strDescriptionEN: String? = null,
    val strAlbumThumb: String? = null,
)

@Serializable
data class TrackResponseDto(
    val track: List<TrackDto>? = null,
)

@Serializable
data class TrackDto(
    val idTrack: String,
    val strTrack: String,
    val strGenre: String? = null,
    val intDuration: String? = null,
    val intTrackNumber: String? = null,
    val intCD: String? = null,
)
