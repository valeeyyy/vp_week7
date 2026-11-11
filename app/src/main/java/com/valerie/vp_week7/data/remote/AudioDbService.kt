package com.valerie.vp_week7.data.remote

import com.valerie.vp_week7.data.dto.AlbumResponseDto
import com.valerie.vp_week7.data.dto.ArtistResponseDto
import com.valerie.vp_week7.data.dto.TrackResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Service layer: daftar endpoint TheAudioDB. Semua fungsi `suspend` -> aman dipanggil
 * dari coroutine (Retrofit sendiri memindahkan pekerjaan jaringan keluar dari Main thread).
 * API key sudah menjadi bagian dari BASE_URL (lihat ServiceLocator).
 */
interface AudioDbService {

    @GET("search.php")
    suspend fun searchArtist(@Query("s") artistName: String): ArtistResponseDto

    @GET("searchalbum.php")
    suspend fun searchAlbums(@Query("s") artistName: String): AlbumResponseDto

    @GET("album.php")
    suspend fun getAlbumDetail(@Query("m") albumId: String): AlbumResponseDto

    @GET("track.php")
    suspend fun getAlbumTracks(@Query("m") albumId: String): TrackResponseDto
}
