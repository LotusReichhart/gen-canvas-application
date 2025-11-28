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
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

class SignInWithEmailUseCaseTest {
    private val authRepository = mockk<AuthRepository>()
    private val userRepository = mockk<UserRepository>()

    private lateinit var signInWithEmailUseCase: SignInWithEmailUseCase

    @Before
    fun setUp() {
        signInWithEmailUseCase = SignInWithEmailUseCase(authRepository, userRepository)
    }

    @Test
    fun `invoke should return User when auth AND fetch profile are successful`() = runTest {
        val email = "test@example.com"
        val password = "password123"
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

        coEvery { authRepository.signInWithEmail(email, password) } returns Result.success(Unit)

        coEvery { userRepository.fetchProfile() } returns Result.success(fakeUser)
        val result = signInWithEmailUseCase(email, password)

        assertTrue(result.isSuccess)
        assertEquals(fakeUser, result.getOrNull())

        coVerify(exactly = 1) { authRepository.signInWithEmail(email, password) }
        coVerify(exactly = 1) { userRepository.fetchProfile() }
    }

    @Test
    fun `invoke should return Failure when signInWithEmail fails`() = runTest {
        val email = "test@example.com"
        val password = "wrong_password"
        val exception = Exception("Sai mật khẩu")

        coEvery { authRepository.signInWithEmail(email, password) } returns Result.failure(exception)

        val result = signInWithEmailUseCase(email, password)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())

        coVerify(exactly = 1) { authRepository.signInWithEmail(email, password) }
        coVerify(exactly = 0) { userRepository.fetchProfile() }
    }

    @Test
    fun `invoke should return Failure when signIn succeeds BUT fetchProfile fails`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val exception = Exception("Lỗi mạng khi lấy profile")

        coEvery { authRepository.signInWithEmail(email, password) } returns Result.success(Unit)
        coEvery { userRepository.fetchProfile() } returns Result.failure(exception)

        val result = signInWithEmailUseCase(email, password)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())

        coVerify(exactly = 1) { authRepository.signInWithEmail(email, password) }
        coVerify(exactly = 1) { userRepository.fetchProfile() }
    }
}