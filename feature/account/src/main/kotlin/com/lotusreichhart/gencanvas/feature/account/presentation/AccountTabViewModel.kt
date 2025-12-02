package com.lotusreichhart.gencanvas.feature.account.presentation

import com.lotusreichhart.gencanvas.core.common.event.GlobalUiEventManager
import com.lotusreichhart.gencanvas.core.domain.usecase.user.GetProfileStreamUseCase
import com.lotusreichhart.gencanvas.core.domain.util.NetworkMonitor
import com.lotusreichhart.gencanvas.core.ui.viewmodel.AuthenticatedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountTabViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    globalUiEventManager: GlobalUiEventManager,
    getProfileStreamUseCase: GetProfileStreamUseCase,
) : AuthenticatedViewModel(
    networkMonitor = networkMonitor,
    globalUiEventManager = globalUiEventManager,
    getProfileStreamUseCase = getProfileStreamUseCase
)