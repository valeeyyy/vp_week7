package com.valerie.vp_week7.data.repository

import com.valerie.vp_week7.data.dto.AlbumDto
import com.valerie.vp_week7.data.local.CatalogAlbum
import com.valerie.vp_week7.data.dto.ArtistDto
import com.valerie.vp_week7.data.dto.TrackDto
import com.valerie.vp_week7.model.Album
import com.valerie.vp_week7.model.AlbumDetail
import com.valerie.vp_week7.model.AlbumWithTracks
import com.valerie.vp_week7.model.Artist
import com.valerie.vp_week7.model.Track

private fun String?.clean(): String? = this?.trim()?.takeIf { it.isNotEmpty() }

fun ArtistDto.toDomain() = Artist(
    id = idArtist,
    name = strArtist,
    genre = strGenre.clean() ?: strStyle.clean() ?: "Unknown",
    country = strCountry.clean().orEmpty(),
    biography = (strBiographyEN.clean() ?: strBiography.clean()).orEmpty(),
    bannerUrl = strArtistBanner.clean() ?: strArtistWideThumb.clean() ?: strArtistFanart.clean(),
    thumbUrl = strArtistThumb.clean(),
)

fun AlbumDto.toDomain() = Album(
    id = idAlbum,
    title = strAlbum,
    artistName = strArtist.orEmpty(),
    year = intYearReleased.clean()?.takeIf { it != "0" }.orEmpty(),
    genre = strGenre.clean() ?: strStyle.clean() ?: "-",
    coverUrl = strAlbumThumb.clean(),
)

fun AlbumDto.toDetail() = AlbumDetail(
    id = idAlbum,
    title = strAlbum,
    artistName = strArtist.orEmpty(),
    year = intYearReleased.clean()?.takeIf { it != "0" }.orEmpty(),
    genre = strGenre.clean() ?: strStyle.clean() ?: "-",
    description = (strDescriptionEN.clean() ?: strDescription.clean()).orEmpty(),
    coverUrl = strAlbumThumb.clean(),
)

fun TrackDto.toDomain(fallbackNumber: Int) = Track(
    id = idTrack,
    number = intTrackNumber?.toIntOrNull()?.takeIf { it > 0 } ?: fallbackNumber,
    title = strTrack,
    genre = strGenre.clean() ?: "-",
    durationMs = intDuration?.toLongOrNull(),
)

fun CatalogAlbum.toAlbum(artistName: String) = Album(
    id = id,
    title = title,
    artistName = artistName,
    year = year,
    genre = genre,
    coverUrl = coverUrl,
    coverRes = coverRes,
)

fun CatalogAlbum.toAlbumWithTracks(artistName: String) = AlbumWithTracks(
    album = AlbumDetail(
        id = id,
        title = title,
        artistName = artistName,
        year = year,
        genre = genre,
        description = description,
        coverUrl = coverUrl,
        coverRes = coverRes,
    ),
    tracks = tracks.mapIndexed { index, t ->
        Track(
            id = "$id-${index + 1}",
            number = index + 1,
            title = t.title,
            genre = genre,
            durationMs = t.durationMs,
        )
    },
)
