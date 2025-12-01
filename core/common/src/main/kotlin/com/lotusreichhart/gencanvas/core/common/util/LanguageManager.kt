package com.lotusreichhart.gencanvas.core.common.util

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LanguageManager {
    fun setLanguage(code: String) {
        val appLocale = LocaleListCompat.forLanguageTags(code)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    fun getCurrentLanguage(): String {
        val currentAppLocales = AppCompatDelegate.getApplicationLocales()
        return if (!currentAppLocales.isEmpty) {
            currentAppLocales[0]?.language ?: "en"
        } else {
            Locale.getDefault().language
        }
    }
}