package com.valerie.vp_week7.util

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

/** Dilempar saat API mengembalikan hasil kosong (mis. {"artists": null}). */
class EmptyResultException(message: String) : Exception(message)

/**
 * Pengganti runCatching untuk coroutine: CancellationException WAJIB dilempar ulang,
 * kalau tidak, pembatalan coroutine (mis. ViewModel dibuang) ikut "tertelan".
 */
suspend fun <T> safeCall(block: suspend () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

/** Ubah exception teknis menjadi pesan yang ramah dibaca pengguna. */
fun Throwable.toUserMessage(): String = when (this) {
    is EmptyResultException -> message ?: "Data tidak ditemukan."
    is HttpException -> when (code()) {
        404 -> "Data tidak ditemukan."
        429 -> "Terlalu banyak permintaan ke server. Tunggu sebentar lalu coba lagi."
        else -> "Server bermasalah (kode ${code()})."
    }
    is UnknownHostException -> message ?: "Tidak ada koneksi internet."
    is SocketTimeoutException -> "Koneksi ke server terlalu lama. Coba lagi."
    is IOException -> message ?: "Tidak ada koneksi internet."
    is SerializationException -> "Format data dari server tidak dikenali."
    else -> message ?: "Terjadi kesalahan."
}
