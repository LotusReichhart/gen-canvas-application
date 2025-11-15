package com.lotusreichhart.data.di

import com.lotusreichhart.domain.repositories.AuthRepository
import com.lotusreichhart.domain.repositories.UserRepository
import com.lotusreichhart.domain.use_cases.auth.RequestForgotPasswordUseCase
import com.lotusreichhart.domain.use_cases.auth.RequestSignUpUseCase
import com.lotusreichhart.domain.use_cases.auth.ResendOtpUseCase
import com.lotusreichhart.domain.use_cases.auth.ResetPasswordUseCase
import com.lotusreichhart.domain.use_cases.auth.SignInWithEmailUseCase
import com.lotusreichhart.domain.use_cases.auth.SignInWithGoogleUseCase
import com.lotusreichhart.domain.use_cases.auth.SignOutUseCase
import com.lotusreichhart.domain.use_cases.auth.VerifyForgotPasswordUseCase
import com.lotusreichhart.domain.use_cases.auth.VerifySignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthUseCaseModule {
    @Provides
    @Singleton
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
    @Singleton
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
    @Singleton
    fun provideRequestSignUpUseCase(
        authRepository: AuthRepository,
    ): RequestSignUpUseCase {
        return RequestSignUpUseCase(
            authRepository = authRepository
        )
    }

    @Provides
    @Singleton
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
    @Singleton
    fun provideRequestForgotPasswordUseCase(
        authRepository: AuthRepository
    ): RequestForgotPasswordUseCase {
        return RequestForgotPasswordUseCase(
            authRepository = authRepository
        )
    }

    @Provides
    @Singleton
    fun provideVerifyForgotPasswordUseCase(
        authRepository: AuthRepository
    ): VerifyForgotPasswordUseCase {
        return VerifyForgotPasswordUseCase(
            authRepository = authRepository
        )
    }

    @Provides
    @Singleton
    fun provideResetPasswordUseCase(
        authRepository: AuthRepository
    ): ResetPasswordUseCase {
        return ResetPasswordUseCase(
            authRepository = authRepository
        )
    }

    @Provides
    @Singleton
    fun provideResendOtpUseCase(
        authRepository: AuthRepository
    ): ResendOtpUseCase {
        return ResendOtpUseCase(
            authRepository = authRepository
        )
    }

    @Provides
    @Singleton
    fun provideSignOutUseCase(
        authRepository: AuthRepository
    ): SignOutUseCase {
        return SignOutUseCase(
            authRepository = authRepository
        )
    }
}