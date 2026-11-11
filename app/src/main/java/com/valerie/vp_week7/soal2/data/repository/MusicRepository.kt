package com.valerie.vp_week7.soal2.data.repository

import com.valerie.vp_week7.soal2.data.local.ColdplayCatalog
import com.valerie.vp_week7.soal2.data.remote.AudioDbService
import com.valerie.vp_week7.soal2.model.AlbumWithTracks
import com.valerie.vp_week7.soal2.model.ArtistProfile
import com.valerie.vp_week7.soal2.util.EmptyResultException
import com.valerie.vp_week7.soal2.util.safeCall
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

class MusicRepository(private val service: AudioDbService) {

    suspend fun getArtistProfile(artistName: String): Result<ArtistProfile> = safeCall {
        coroutineScope {
            val artistDeferred = async { service.searchArtist(artistName) }
            val albumsDeferred = async { service.searchAlbums(artistName) }

            val artist = artistDeferred.await().artists?.firstOrNull()
                ?: throw EmptyResultException("Artis \"$artistName\" tidak ditemukan.")
            val apiAlbums = albumsDeferred.await().album.orEmpty().map { it.toDomain() }

            val albums = if (ColdplayCatalog.isCatalogArtist(artist.strArtist)) {

                val catalog = ColdplayCatalog.albums.map { it.toAlbum(artist.strArtist) }
                (catalog + apiAlbums).distinctBy { it.id }
            } else {
                apiAlbums.distinctBy { it.id }
            }

            ArtistProfile(artist.toDomain(), albums)
        }
    }

    suspend fun getAlbumWithTracks(albumId: String): Result<AlbumWithTracks> = safeCall {
        ColdplayCatalog.find(albumId)?.let { local ->
            delay(CATALOG_LOAD_DELAY_MS)
            return@safeCall local.toAlbumWithTracks(ColdplayCatalog.ARTIST_NAME)
        }

        coroutineScope {
            val albumDeferred = async { service.getAlbumDetail(albumId) }
            val tracksDeferred = async { service.getAlbumTracks(albumId) }

            val album = albumDeferred.await().album?.firstOrNull()
                ?: throw EmptyResultException("Detail album tidak ditemukan.")
            val tracks = tracksDeferred.await().track.orEmpty()
                .mapIndexed { index, dto -> dto.toDomain(index + 1) }
                .sortedBy { it.number }

            AlbumWithTracks(album.toDetail(), tracks)
        }
    }

    private companion object {
        const val CATALOG_LOAD_DELAY_MS = 900L
    }
}
