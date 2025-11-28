package com.lotusreichhart.gencanvas.core.domain.usecase.user

import com.lotusreichhart.gencanvas.core.domain.repository.UserRepository
import javax.inject.Inject

class ClearUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() {
        userRepository.clearUserProfile()
    }
}