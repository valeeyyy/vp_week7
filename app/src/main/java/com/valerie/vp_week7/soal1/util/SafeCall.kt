package com.valerie.vp_week7.soal1.util

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.cancellation.CancellationException

class EmptyResultException(message: String) : Exception(message)

suspend fun <T> safeCall(block: suspend () -> T): Result<T> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

data class UiError(
    val title: String,
    val message: String,
    val isEmpty: Boolean = false,
)

fun Throwable.toUiError(): UiError = when (this) {
    is EmptyResultException -> UiError("No Cards Found", message ?: "Try another search.", isEmpty = true)
    is HttpException -> UiError(
        title = "Connection Failed",
        message = when (code()) {
            429 -> "HTTP 429 · Daily API limit reached"
            else -> "HTTP ${code()}"
        },
    )
    is SocketTimeoutException -> UiError("Connection Failed", "Request timed out")
    is IOException -> UiError("Connection Failed", "HTTP 401")
    is SerializationException -> UiError("Connection Failed", "Unexpected response from server")
    else -> UiError("Connection Failed", message ?: "Something went wrong")
}
