package com.lotusreichhart.domain.exception

data class ServerException(
    override val message: String,
    val fieldErrors: Map<String, String>? = null
) : Exception(message)
