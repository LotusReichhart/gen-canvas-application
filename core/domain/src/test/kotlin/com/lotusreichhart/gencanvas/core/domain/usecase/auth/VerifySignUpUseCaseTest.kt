package com.lotusreichhart.gencanvas.core.domain.usecase.auth

import com.lotusreichhart.gencanvas.core.domain.repository.AuthRepository
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

class VerifySignUpUseCaseTest {
    private val authRepository = mockk<AuthRepository>()
    private val userRepository = mockk<UserRepository>()

    private lateinit var verifySignUpUseCase: VerifySignUpUseCase

    @Before
    fun setUp() {
        verifySignUpUseCase = VerifySignUpUseCase(authRepository, userRepository)
    }

    @Test
    fun `invoke should return User when verify AND fetch profile are successful`() = runTest {
        // GIVEN
        val otp = "123456"
        val email = "test@example.com"
        val fakeUser = User(
            id = 1,
            name = "Test User",
            email = email,
            avatarUrl = null,
            status = UserStatus.ACTIVE,
            tier = UserTier.FREE,
            authProvider = AuthProvider.EMAIL,
            balance = 0,
            lastRefillDate = null
        )

        coEvery { authRepository.verifySignUp(otp, email) } returns Result.success(Unit)
        coEvery { userRepository.fetchProfile() } returns Result.success(fakeUser)

        val result = verifySignUpUseCase(otp, email)

        assertTrue(result.isSuccess)
        assertEquals(fakeUser, result.getOrNull())

        coVerify(exactly = 1) { authRepository.verifySignUp(otp, email) }
        coVerify(exactly = 1) { userRepository.fetchProfile() }
    }

    @Test
    fun `invoke should return Failure when verifySignUp fails`() = runTest {
        val otp = "000000"
        val email = "test@example.com"
        val exception = Exception("Mã OTP không đúng")

        coEvery { authRepository.verifySignUp(otp, email) } returns Result.failure(exception)

        val result = verifySignUpUseCase(otp, email)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())

        coVerify(exactly = 1) { authRepository.verifySignUp(otp, email) }
        coVerify(exactly = 0) { userRepository.fetchProfile() }
    }

    @Test
    fun `invoke should return Failure when verify succeeds BUT fetchProfile fails`() = runTest {
        val otp = "123456"
        val email = "test@example.com"
        val exception = Exception("Lỗi mạng khi lấy thông tin user")

        coEvery { authRepository.verifySignUp(otp, email) } returns Result.success(Unit)
        coEvery { userRepository.fetchProfile() } returns Result.failure(exception)

        val result = verifySignUpUseCase(otp, email)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())

        coVerify(exactly = 1) { authRepository.verifySignUp(otp, email) }
        coVerify(exactly = 1) { userRepository.fetchProfile() }
    }
}