package com.loki.plitso.util

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

sealed class Resource<out T> {
    class Loading<out T>() : Resource<T>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error<out T>(val message: String) : Resource<T>()
}

inline fun <K> safeApiCall(
    crossinline apiCall: suspend () -> K,
    crossinline onError: (HttpException) -> String = { "Something went wrong" }
): Flow<Resource<K>> = flow {
    try {
        emit(Resource.Loading())
        val result = apiCall()
        emit(Resource.Success(result))
    } catch (e: HttpException) {
        Log.d("API Error", e.localizedMessage ?: "something went wrong")
        emit(Resource.Error(onError(e)))
    } catch (e: IOException) {
        Timber.tag("API Error").d(e)
        emit(Resource.Error("No network connection"))
    }
}.flowOn(Dispatchers.IO)