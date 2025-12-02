package com.lotusreichhart.gencanvas.core.data.network.util

import com.lotusreichhart.gencanvas.core.common.R
import com.lotusreichhart.gencanvas.core.common.util.TextResource

fun Throwable.asUiText(): TextResource {
    return when (this) {
        is ServerException -> this.textResource
        else -> this.message?.let { TextResource.Raw(it) }
            ?: TextResource.Id(R.string.core_unknow_error)
    }
}