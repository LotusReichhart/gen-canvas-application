package com.lotusreichhart.gencanvas.core.data.network.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lotusreichhart.gencanvas.core.data.network.model.ResponseWrapper
import com.lotusreichhart.gencanvas.core.model.exception.ServerException
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
            val errorMessage = wrapper.message ?: "Lỗi không xác định."
            Result.failure(ServerException(errorMessage, wrapper.errors))
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
            val errorMessage = wrapper.message ?: "Yêu cầu thất bại."
            Result.failure(ServerException(errorMessage, wrapper.errors))
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
                        ?: "Lỗi máy chủ. Vui lòng thử lại sau."

                    return Result.failure(
                        ServerException(
                            message = serverMessage,
                            fieldErrors = errorResponse.errors
                        )
                    )
                } catch (parseException: Exception) {
                    Timber.e(parseException, "parseException")
                }
            }

            val message = when (e.code()) {
                404 -> "Không tìm thấy tài nguyên."
                429 -> "Quá nhiều yêu cầu. Vui lòng thử lại sau."
                in 500..599 -> "Lỗi máy chủ. Vui lòng thử lại sau."
                else -> "Đã xảy ra sự cố không xác định"
            }
            Result.failure(Exception(message, e))
        }

        is IOException -> {
            Timber.e(e, "exception is IOException")
            Result.failure(Exception("Không thể kết nối máy chủ.", e))
        }

        else -> {
            Timber.e(e, "exception is Exception")
            Result.failure(Exception("Đã xảy ra lỗi không xác định", e))
        }
    }
}