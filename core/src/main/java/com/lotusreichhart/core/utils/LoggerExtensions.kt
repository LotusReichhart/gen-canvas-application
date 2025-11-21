package com.lotusreichhart.core.utils

import timber.log.Timber

inline fun <reified T> T.logD(message: String) {
    val tag = T::class.java.simpleName
    Timber.tag(tag).d(message)
}

inline fun <reified T> T.logE(message: String, throwable: Throwable? = null) {
    val tag = T::class.java.simpleName
    if (throwable != null) {
        Timber.tag(tag).e(throwable, message)
    } else {
        Timber.tag(tag).e(message)
    }
}

inline fun <reified T> T.logI(message: String) {
    val tag = T::class.java.simpleName
    Timber.tag(tag).i(message)
}

inline fun <reified T> T.logW(message: String) {
    val tag = T::class.java.simpleName
    Timber.tag(tag).w(message)
}

fun logD(tag: String, message: String) {
    Timber.tag(tag).d(message)
}

fun logE(tag: String, message: String, throwable: Throwable? = null) {
    if (throwable != null) {
        Timber.tag(tag).e(throwable, message)
    } else {
        Timber.tag(tag).e(message)
    }
}

fun logI(tag: String, message: String) {
    Timber.tag(tag).i(message)
}

fun logW(tag: String, message: String) {
    Timber.tag(tag).w(message)
}