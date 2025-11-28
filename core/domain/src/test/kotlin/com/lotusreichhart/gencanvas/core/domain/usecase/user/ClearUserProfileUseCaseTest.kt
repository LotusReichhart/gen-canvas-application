package com.lotusreichhart.gencanvas.core.domain.usecase.user

import com.lotusreichhart.gencanvas.core.domain.repository.UserRepository
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ClearUserProfileUseCaseTest {
    private val userRepository = mockk<UserRepository>()
    private lateinit var clearUserProfileUseCase: ClearUserProfileUseCase

    @Before
    fun setUp() {
        clearUserProfileUseCase = ClearUserProfileUseCase(userRepository)
    }

    @Test
    fun `invoke should call clearUserProfile in repository`() = runTest {
        coJustRun { userRepository.clearUserProfile() }

        clearUserProfileUseCase()

        coVerify(exactly = 1) { userRepository.clearUserProfile() }
    }
}