package com.bpb.android.clips.repository.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    class Success<out T : Any>(val data: T) : Result<T>()

    class Error(val exception: Throwable) : Result<Nothing>()

    fun getOrNull(): T? =
        when (this) {
            is Success -> this.data
            else -> null
        }

    fun exceptionOrNull(): Throwable? =
        when (this) {
            is Error -> this.exception
            else -> null
        }

    companion object {

        fun <T : Any> success(data: T): Result<T> = Success(data)

        fun failed(exception: Throwable): Result<Nothing> = Error(exception)
    }
}