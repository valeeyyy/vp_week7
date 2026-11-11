package com.valerie.vp_week7.data.repository

import com.valerie.vp_week7.data.local.ColdplayCatalog
import com.valerie.vp_week7.data.remote.AudioDbService
import com.valerie.vp_week7.model.AlbumWithTracks
import com.valerie.vp_week7.model.ArtistProfile
import com.valerie.vp_week7.util.EmptyResultException
import com.valerie.vp_week7.util.safeCall
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

/**
 * Repository: satu-satunya pintu ViewModel ke data. Memanggil Service, mengubah DTO
 * menjadi model domain, dan membungkus hasil dalam Result (sukses / gagal).
 *
 * Untuk Coldplay, daftar album & tracklist dilengkapi dari [ColdplayCatalog] karena
 * public test key TheAudioDB hanya mengembalikan 1 album / 1 lagu.
 */
class MusicRepository(private val service: AudioDbService) {

    /** Info artis + daftar album diambil paralel (2 request sekaligus). */
    suspend fun getArtistProfile(artistName: String): Result<ArtistProfile> = safeCall {
        coroutineScope {
            val artistDeferred = async { service.searchArtist(artistName) }
            val albumsDeferred = async { service.searchAlbums(artistName) }

            val artist = artistDeferred.await().artists?.firstOrNull()
                ?: throw EmptyResultException("Artis \"$artistName\" tidak ditemukan.")
            val apiAlbums = albumsDeferred.await().album.orEmpty().map { it.toDomain() }

            val albums = if (ColdplayCatalog.isCatalogArtist(artist.strArtist)) {
                // Katalog lebih dulu (urutan sesuai desain), lalu album API yang belum ada
                val catalog = ColdplayCatalog.albums.map { it.toAlbum(artist.strArtist) }
                (catalog + apiAlbums).distinctBy { it.id }
            } else {
                apiAlbums.distinctBy { it.id }
            }

            ArtistProfile(artist.toDomain(), albums)
        }
    }

    /** Detail album + tracklist. Album katalog dibaca lokal, selain itu dari API (paralel). */
    suspend fun getAlbumWithTracks(albumId: String): Result<AlbumWithTracks> = safeCall {
        ColdplayCatalog.find(albumId)?.let { local ->
            delay(CATALOG_LOAD_DELAY_MS) // jeda singkat supaya loading state tetap terlihat
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
