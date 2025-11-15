package com.lotusreichhart.domain.exceptions

data class ServerException(
    override val message: String,
    val fieldErrors: Map<String, String>? = null
) : Exception(message)
