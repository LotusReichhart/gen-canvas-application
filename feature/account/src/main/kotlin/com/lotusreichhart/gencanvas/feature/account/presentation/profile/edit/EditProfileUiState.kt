package com.lotusreichhart.gencanvas.feature.account.presentation.profile.edit


import android.net.Uri

internal data class EditProfileUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,

    val name: String = "",
    val avatarUri: Uri? = null,

    val originalName: String = "",
    val originalAvatarUrl: String? = null,

    val nameErrorMessage: String? = null
) {
    val isChanged: Boolean
        get() {
            val nameChanged = name.trim() != originalName.trim()
            val avatarChanged = avatarUri != null
            return nameChanged || avatarChanged
        }

    val displayAvatarModel: Any?
        get() = avatarUri ?: originalAvatarUrl
}
