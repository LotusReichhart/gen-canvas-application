package com.lotusreichhart.gencanvas.core.data.network.util

import com.lotusreichhart.gencanvas.core.common.util.TextResource

data class ServerException(
    val textResource: TextResource,
    val fieldErrors: Map<String, String>? = null
) : Exception("Server Error occurred")