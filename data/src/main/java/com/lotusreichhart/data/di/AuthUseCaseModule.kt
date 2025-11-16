package com.lotusreichhart.data.di

import com.lotusreichhart.domain.repository.AuthRepository
import com.lotusreichhart.domain.repository.UserRepository
import com.lotusreichhart.domain.use_case.auth.RequestForgotPasswordUseCase
import com.lotusreichhart.domain.use_case.auth.RequestSignUpUseCase
import com.lotusreichhart.domain.use_case.auth.ResendOtpUseCase
import com.lotusreichhart.domain.use_case.auth.ResetPasswordUseCase
import com.lotusreichhart.domain.use_case.auth.SignInWithEmailUseCase
import com.lotusreichhart.domain.use_case.auth.SignInWithGoogleUseCase
import com.lotusreichhart.domain.use_case.auth.SignOutUseCase
import com.lotusreichhart.domain.use_case.auth.VerifyForgotPasswordUseCase
import com.lotusreichhart.domain.use_case.auth.VerifySignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthUseCaseModule {
    @Provides
    fun provideSignInWithEmailUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ): SignInWithEmailUseCase {
        return SignInWithEmailUseCase(
            authRepository = authRepository,
            userRepository = userRepository
        )
    }

    @Provides
    fun provideSignInWithGoogleUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ): SignInWithGoogleUseCase {
        return SignInWithGoogleUseCase(
            authRepository = authRepository,
            userRepository = userRepository
        )
    }

    @Provides
    fun provideRequestSignUpUseCase(
        authRepository: AuthRepository,
    ): RequestSignUpUseCase {
        return RequestSignUpUseCase(
            authRepository = authRepository
        )
    }

    @Provides
    fun provideVerifySignUpUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ): VerifySignUpUseCase {
        return VerifySignUpUseCase(
            authRepository = authRepository,
            userRepository = userRepository
        )
    }

    @Provides
    fun provideRequestForgotPasswordUseCase(
        authRepository: AuthRepository
    ): RequestForgotPasswordUseCase {
        return RequestForgotPasswordUseCase(
            authRepository = authRepository
        )
    }

    @Provides
    fun provideVerifyForgotPasswordUseCase(
        authRepository: AuthRepository
    ): VerifyForgotPasswordUseCase {
        return VerifyForgotPasswordUseCase(
            authRepository = authRepository
        )
    }

    @Provides
    fun provideResetPasswordUseCase(
        authRepository: AuthRepository
    ): ResetPasswordUseCase {
        return ResetPasswordUseCase(
            authRepository = authRepository
        )
    }

    @Provides
    fun provideResendOtpUseCase(
        authRepository: AuthRepository
    ): ResendOtpUseCase {
        return ResendOtpUseCase(
            authRepository = authRepository
        )
    }

    @Provides
    fun provideSignOutUseCase(
        authRepository: AuthRepository
    ): SignOutUseCase {
        return SignOutUseCase(
            authRepository = authRepository
        )
    }
}