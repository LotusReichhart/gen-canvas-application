package com.lotusreichhart.data.di

import com.lotusreichhart.domain.repositories.UserRepository
import com.lotusreichhart.domain.use_cases.user.ClaimAdRewardUseCase
import com.lotusreichhart.domain.use_cases.user.FetchUserProfileUseCase
import com.lotusreichhart.domain.use_cases.user.GetUserProfileFlowUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserUseCaseModule {

    @Provides
    @Singleton
    fun provideGetUserProfileFlowUseCase(
        userRepository: UserRepository
    ): GetUserProfileFlowUseCase {
        return GetUserProfileFlowUseCase(
            userRepository = userRepository
        )
    }

    @Provides
    @Singleton
    fun provideFetchUserProfileUseCase(
        userRepository: UserRepository
    ): FetchUserProfileUseCase {
        return FetchUserProfileUseCase(
            userRepository = userRepository
        )
    }

    @Provides
    @Singleton
    fun provideClaimAdRewardUseCase(
        userRepository: UserRepository
    ): ClaimAdRewardUseCase {
        return ClaimAdRewardUseCase(
            userRepository = userRepository
        )
    }
}