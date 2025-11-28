package com.lotusreichhart.gencanvas.core.domain.usecase.auth

import com.lotusreichhart.gencanvas.core.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RequestSignUpUseCaseTest {
    private val authRepository = mockk<AuthRepository>()
    private lateinit var requestSignUpUseCase: RequestSignUpUseCase

    @Before
    fun setUp() {
        requestSignUpUseCase = RequestSignUpUseCase(authRepository)
    }

    @Test
    fun `invoke should return Success when repository returns Success`() = runTest {
        val name = "Test User"
        val email = "test@example.com"
        val password = "password123"

        coEvery { authRepository.requestSignUp(name, email, password) } returns Result.success(Unit)

        val result = requestSignUpUseCase(name, email, password)

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { authRepository.requestSignUp(name, email, password) }
    }

    @Test
    fun `invoke should return Failure when repository returns Failure`() = runTest {
        val name = "Test User"
        val email = "test@example.com"
        val password = "password123"
        val exception = Exception("Email đã tồn tại")

        coEvery { authRepository.requestSignUp(name, email, password) } returns Result.failure(exception)

        val result = requestSignUpUseCase(name, email, password)

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify(exactly = 1) { authRepository.requestSignUp(name, email, password) }
    }
}