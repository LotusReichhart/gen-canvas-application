package com.lotusreichhart.gencanvas.core.domain.usecase.user

import com.lotusreichhart.gencanvas.core.domain.repository.UserRepository
import com.lotusreichhart.gencanvas.core.model.user.User
import com.lotusreichhart.gencanvas.core.model.user.enums.AuthProvider
import com.lotusreichhart.gencanvas.core.model.user.enums.UserStatus
import com.lotusreichhart.gencanvas.core.model.user.enums.UserTier
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FetchProfileUseCaseTest {
    private val userRepository = mockk<UserRepository>()
    private lateinit var fetchProfileUseCase: FetchProfileUseCase

    private val fakeUser = User(
        id = 1,
        name = "Test User",
        email = "test@example.com",
        avatarUrl = null,
        status = UserStatus.ACTIVE,
        tier = UserTier.FREE,
        authProvider = AuthProvider.EMAIL,
        balance = 10,
        lastRefillDate = null
    )

    @Before
    fun setUp() {
        fetchProfileUseCase = FetchProfileUseCase(userRepository)
    }

    @Test
    fun `invoke should return Success when repository returns Success`() = runTest {
        coEvery { userRepository.fetchProfile() } returns Result.success(fakeUser)

        val result = fetchProfileUseCase()

        assertTrue(result.isSuccess)
        assertEquals(fakeUser, result.getOrNull())
        coVerify(exactly = 1) { userRepository.fetchProfile() }
    }

    @Test
    fun `invoke should return Failure when repository returns Failure`() = runTest {
        val exception = Exception("Lỗi mạng")
        coEvery { userRepository.fetchProfile() } returns Result.failure(exception)

        val result = fetchProfileUseCase()

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify(exactly = 1) { userRepository.fetchProfile() }
    }
}