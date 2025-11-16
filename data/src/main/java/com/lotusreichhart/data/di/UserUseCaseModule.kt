package com.lotusreichhart.data.di

import com.lotusreichhart.domain.repository.UserRepository
import com.lotusreichhart.domain.use_case.user.ClaimAdRewardUseCase
import com.lotusreichhart.domain.use_case.user.FetchUserProfileUseCase
import com.lotusreichhart.domain.use_case.user.GetUserProfileFlowUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserUseCaseModule {

    @Provides
    fun provideGetUserProfileFlowUseCase(
        userRepository: UserRepository
    ): GetUserProfileFlowUseCase {
        return GetUserProfileFlowUseCase(
            userRepository = userRepository
        )
    }

    @Provides
    fun provideFetchUserProfileUseCase(
        userRepository: UserRepository
    ): FetchUserProfileUseCase {
        return FetchUserProfileUseCase(
            userRepository = userRepository
        )
    }

    @Provides
    fun provideClaimAdRewardUseCase(
        userRepository: UserRepository
    ): ClaimAdRewardUseCase {
        return ClaimAdRewardUseCase(
            userRepository = userRepository
        )
    }
}