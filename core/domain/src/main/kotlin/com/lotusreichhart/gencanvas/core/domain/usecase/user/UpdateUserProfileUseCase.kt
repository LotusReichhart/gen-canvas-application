package com.lotusreichhart.gencanvas.core.domain.usecase.user

import com.lotusreichhart.gencanvas.core.domain.repository.UserRepository
import com.lotusreichhart.gencanvas.core.model.user.User
import java.io.File
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(name: String?, avatarFile: File?): Result<User> {
        return userRepository.updateUserProfile(name, avatarFile)
    }
}