package com.valerie.movie_db.data.repository

import com.valerie.movie_db.BuildConfig
import com.valerie.movie_db.data.remote.TmdbApi
import com.valerie.movie_db.model.MovieCategory
import com.valerie.movie_db.model.MovieDetail
import com.valerie.movie_db.model.MoviePage
import com.valerie.movie_db.util.safeCall
class MovieRepository(private val api: TmdbApi) {

    /** Movie untuk carousel horizontal (Popular / Now Playing). */
    suspend fun getFeatured(category: MovieCategory): Result<MoviePage> = guarded {
        when (category) {
            MovieCategory.POPULAR -> api.getPopular()
            MovieCategory.NOW_PLAYING -> api.getNowPlaying()
        }.toDomain()
    }

    /** Seluruh movie (list vertikal), per halaman. */
    suspend fun getAllMovies(page: Int): Result<MoviePage> = guarded {
        api.discoverMovies(page).toDomain()
    }

    suspend fun getMovieDetail(id: Int): Result<MovieDetail> = guarded {
        api.getMovieDetail(id).toDomain()
    }

    private suspend fun <T> guarded(block: suspend () -> T): Result<T> {
        if (BuildConfig.TMDB_API_KEY.isBlank()) {
            return Result.failure(
                IllegalStateException("TMDB_API_KEY belum diisi. Tambahkan di local.properties lalu sync Gradle.")
            )
        }
        return safeCall(block)
    }
}