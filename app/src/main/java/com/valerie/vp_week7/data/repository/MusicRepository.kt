package com.valerie.vp_week7.data.repository

import com.valerie.vp_week7.data.remote.AudioDbService
import com.valerie.vp_week7.model.AlbumWithTracks
import com.valerie.vp_week7.model.ArtistProfile
import com.valerie.vp_week7.util.EmptyResultException
import com.valerie.vp_week7.util.safeCall
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Repository: satu-satunya pintu ViewModel ke data. Memanggil Service, mengubah DTO
 * menjadi model domain, dan membungkus hasil dalam Result (sukses / gagal).
 */
class MusicRepository(private val service: AudioDbService) {

    /** Info artis + daftar album diambil paralel (2 request sekaligus). */
    suspend fun getArtistProfile(artistName: String): Result<ArtistProfile> = safeCall {
        coroutineScope {
            val artistDeferred = async { service.searchArtist(artistName) }
            val albumsDeferred = async { service.searchAlbums(artistName) }

            val artist = artistDeferred.await().artists?.firstOrNull()
                ?: throw EmptyResultException("Artis \"$artistName\" tidak ditemukan.")
            val albums = albumsDeferred.await().album.orEmpty()
                .map { it.toDomain() }
                .distinctBy { it.id }

            ArtistProfile(artist.toDomain(), albums)
        }
    }

    /** Detail album + tracklist diambil paralel. */
    suspend fun getAlbumWithTracks(albumId: String): Result<AlbumWithTracks> = safeCall {
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
}
