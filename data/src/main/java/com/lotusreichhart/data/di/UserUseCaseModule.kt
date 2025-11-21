package com.lotusreichhart.data.di

import com.lotusreichhart.domain.repository.UserRepository
import com.lotusreichhart.domain.usecase.user.ClearUserProfileUseCase
import com.lotusreichhart.domain.usecase.user.FetchProfileUseCase
import com.lotusreichhart.domain.usecase.user.GetProfileStreamUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserUseCaseModule {

    @Provides
    fun provideClearUserProfileUseCase(
        userRepository: UserRepository
    ): ClearUserProfileUseCase {
        return ClearUserProfileUseCase(userRepository)
    }

    @Provides
    fun provideFetchProfileUseCase(
        userRepository: UserRepository
    ): FetchProfileUseCase {
        return FetchProfileUseCase(userRepository)
    }

    @Provides
    fun provideGetProfileStreamUseCase(
        userRepository: UserRepository
    ): GetProfileStreamUseCase {
        return GetProfileStreamUseCase(userRepository)
    }

}