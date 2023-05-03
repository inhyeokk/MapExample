package com.example.map.extension

import retrofit2.Response

suspend fun <T> execute(apiCall: suspend () -> Response<T>): Result<T> {
    return try {
        val response = apiCall.invoke()
        if (response.isSuccessful) {
            Result.success(response.body()!!)
        } else {
            Result.failure(Throwable(response.errorBody()?.toString()))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
