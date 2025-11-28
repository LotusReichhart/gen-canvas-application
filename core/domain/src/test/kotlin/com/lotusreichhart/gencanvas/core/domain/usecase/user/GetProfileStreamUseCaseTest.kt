package com.lotusreichhart.gencanvas.core.domain.usecase.user

import com.lotusreichhart.gencanvas.core.domain.repository.UserRepository
import com.lotusreichhart.gencanvas.core.model.user.User
import com.lotusreichhart.gencanvas.core.model.user.enums.AuthProvider
import com.lotusreichhart.gencanvas.core.model.user.enums.UserStatus
import com.lotusreichhart.gencanvas.core.model.user.enums.UserTier
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetProfileStreamUseCaseTest {
    private val userRepository = mockk<UserRepository>()
    private lateinit var getProfileStreamUseCase: GetProfileStreamUseCase

    @Before
    fun setUp() {
        getProfileStreamUseCase = GetProfileStreamUseCase(userRepository)
    }

    @Test
    fun `invoke should return correct Flow from repository`() = runTest {
        // GIVEN
        val fakeUser = User(
            id = 1,
            name = "Stream User",
            email = "stream@example.com",
            avatarUrl = null,
            status = UserStatus.ACTIVE,
            tier = UserTier.PRO,
            authProvider = AuthProvider.GOOGLE,
            balance = 100,
            lastRefillDate = null
        )

        every { userRepository.getProfileStream() } returns flowOf(fakeUser)

        val resultFlow = getProfileStreamUseCase()

        val resultUser = resultFlow.first()

        assertEquals(fakeUser, resultUser)
        verify(exactly = 1) { userRepository.getProfileStream() }
    }

    @Test
    fun `invoke should return null Flow when repository emits null`() = runTest {
        every { userRepository.getProfileStream() } returns flowOf(null)

        val resultFlow = getProfileStreamUseCase()
        val resultUser = resultFlow.first()

        assertEquals(null, resultUser)
    }
}