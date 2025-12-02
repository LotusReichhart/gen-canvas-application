package com.lotusreichhart.gencanvas.core.data.network.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lotusreichhart.gencanvas.core.common.R
import com.lotusreichhart.gencanvas.core.common.util.TextResource
import com.lotusreichhart.gencanvas.core.data.network.model.ResponseWrapper
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

suspend fun <T> safeApiCallData(
    apiCall: suspend () -> ResponseWrapper<T>
): Result<T> {
    return try {
        val wrapper = apiCall()
        Timber.d("safeApiCallData running....: $wrapper")

        if (wrapper.status in 200..299 && wrapper.data != null) {
            Result.success(wrapper.data)
        } else {
            val errorResource = wrapper.message?.let { TextResource.Raw(it) }
                ?: TextResource.Id(R.string.core_unknow_error)
            Result.failure(ServerException(errorResource, wrapper.errors))
        }
    } catch (e: Exception) {
        Timber.e(e, "safeApiCallData exception")
        handleException(e)
    }
}

suspend fun safeApiCallUnit(
    apiCall: suspend () -> ResponseWrapper<Unit>
): Result<Unit> {
    return try {
        val wrapper = apiCall()
        Timber.d("safeApiCallUnit running....: $wrapper")

        if (wrapper.status in 200..299) {
            Result.success(Unit)
        } else {
            val errorResource = wrapper.message?.let { TextResource.Raw(it) }
                ?: TextResource.Id(R.string.core_error_request_failed)

            Result.failure(ServerException(errorResource, wrapper.errors))
        }
    } catch (e: Exception) {
        Timber.e(e, "safeApiCallUnit exception")
        handleException(e)
    }
}

private fun <T> handleException(e: Exception): Result<T> {
    return when (e) {
        is HttpException -> {
            Timber.w(e, "exception is HttpException")

            val errorBody = try {
                e.response()?.errorBody()?.string()
            } catch (ex: Exception) {
                null
            }

            if (!errorBody.isNullOrBlank()) {
                try {
                    val gson = Gson()
                    val type = object : TypeToken<ResponseWrapper<Any>>() {}.type
                    val errorResponse: ResponseWrapper<Any> = gson.fromJson(errorBody, type)

                    val serverMessage = errorResponse.errors?.get("message")
                        ?: errorResponse.message

                    if (serverMessage != null) {
                        return Result.failure(
                            ServerException(
                                textResource = TextResource.Raw(serverMessage),
                                fieldErrors = errorResponse.errors
                            )
                        )
                    }

                    return Result.failure(
                        ServerException(TextResource.Id(R.string.core_error_server))
                    )
                } catch (parseException: Exception) {
                    Timber.e(parseException, "parseException")
                }
            }

            val errorResource = when (e.code()) {
                404 -> TextResource.Id(R.string.core_error_not_found)
                429 -> TextResource.Id(R.string.core_error_too_many_requests)
                in 500..599 -> TextResource.Id(R.string.core_error_server)
                else -> TextResource.Id(R.string.core_unknow_error)
            }
            Result.failure(ServerException(errorResource))
        }

        is IOException -> {
            Timber.e(e, "exception is IOException")
            Result.failure(ServerException(TextResource.Id(R.string.core_error_server)))
        }

        else -> {
            Timber.e(e, "exception is Exception")
            Result.failure(ServerException(TextResource.Id(R.string.core_unknow_error)))
        }
    }
}