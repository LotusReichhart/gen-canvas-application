package com.lotusreichhart.data.di

import com.lotusreichhart.data.monitor.ConnectivityManagerNetworkMonitor
import com.lotusreichhart.domain.monitor.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MonitorModule {

    @Binds
    @Singleton
    abstract fun bindNetworkMonitor(
        impl: ConnectivityManagerNetworkMonitor
    ): NetworkMonitor
}