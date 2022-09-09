package com.bpb.android.clips.repository.ext

import com.bpb.android.clips.repository.data.Result
import com.bpb.android.clips.repository.network.ApiResponse
import kotlinx.coroutines.Deferred

suspend fun <T : Any> Deferred<ApiResponse<T>>.getResult(): Result<T> = getResult { it }

suspend fun <T, R : Any> Deferred<ApiResponse<T>>.getResult(mapper: (T) -> R): Result<R> =
    getResult(mapper) { RuntimeException(it.message()) }

suspend fun <T, R : Any> Deferred<ApiResponse<T>>.getResult(
    successMapper: (T) -> R,
    errorMapper: (ApiResponse<T>) -> Exception
): Result<R> =
    try {
        val response = await()
        val responseBody = response.body()
        if (response.isSuccessful() && responseBody != null) {
            Result.success(successMapper.invoke(responseBody))
        } else {
            Result.failed(errorMapper.invoke(response))
        }
    } catch (e: Exception) {
        Result.failed(e)
    }