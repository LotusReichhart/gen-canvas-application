package com.lotusreichhart.data.utils

import com.lotusreichhart.data.remote.dto.ResponseWrapper
import com.lotusreichhart.domain.exception.ServerException

suspend fun <T> safeApiCallData(
    apiCall: suspend () -> ResponseWrapper<T>
): Result<T> {
    return try {
        // 1. Gọi API
        val wrapper = apiCall()

        // 2. Kiểm tra chung (status == 200 và data không null)
        if (wrapper.status == 200 && wrapper.data != null) {
            // Thành công, trả về dữ liệu 'data'
            Result.success(wrapper.data)
        } else {
            // 3. Thất bại từ server (ví dụ: 400, 404)
            val errorMessage = wrapper.message ?: "Lỗi không xác định."
            Result.failure(ServerException(errorMessage, wrapper.errors))
        }
    } catch (e: Exception) {
        // 4. Lỗi mạng (offline) hoặc lỗi parse JSON
        Result.failure(e)
    }
}

/**
 * === HÀM MỚI (Từ AuthRepositoryImpl) ===
 * Hàm an toàn (safe call) cho các API chỉ trả về Unit (không có data).
 * Nó chỉ kiểm tra 'status == 200'.
 */
suspend fun safeApiCallUnit(
    apiCall: suspend () -> ResponseWrapper<Unit>
): Result<Unit> {
    return try {
        val wrapper = apiCall()
        if (wrapper.status == 200) {
            Result.success(Unit)
        } else {
            val errorMessage = wrapper.message ?: "Yêu cầu thất bại."
            Result.failure(ServerException(errorMessage, wrapper.errors))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}