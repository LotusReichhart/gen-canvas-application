package com.lotusreichhart.gencanvas.core.data.network.di

import com.lotusreichhart.gencanvas.core.data.network.util.ConnectivityManagerNetworkMonitor
import com.lotusreichhart.gencanvas.core.domain.util.NetworkMonitor
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