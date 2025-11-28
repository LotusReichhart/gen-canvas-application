package com.lotusreichhart.gencanvas.core.domain.usecase.user

import com.lotusreichhart.gencanvas.core.domain.repository.UserRepository
import com.lotusreichhart.gencanvas.core.model.user.User
import javax.inject.Inject

class FetchProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<User> {
        return userRepository.fetchProfile()
    }
}