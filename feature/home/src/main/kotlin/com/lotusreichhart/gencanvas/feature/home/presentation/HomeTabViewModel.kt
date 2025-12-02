package com.lotusreichhart.gencanvas.feature.home.presentation

import androidx.lifecycle.viewModelScope
import com.lotusreichhart.gencanvas.core.common.event.GlobalUiEventManager
import com.lotusreichhart.gencanvas.core.common.event.UiEvent
import com.lotusreichhart.gencanvas.core.common.util.TextResource
import com.lotusreichhart.gencanvas.core.data.network.util.ServerException
import com.lotusreichhart.gencanvas.core.data.network.util.asUiText
import com.lotusreichhart.gencanvas.core.domain.usecase.banner.GetListBannerUseCase
import com.lotusreichhart.gencanvas.core.domain.usecase.banner.RefreshBannersUseCase
import com.lotusreichhart.gencanvas.core.domain.usecase.user.GetProfileStreamUseCase
import com.lotusreichhart.gencanvas.core.domain.util.NetworkMonitor
import com.lotusreichhart.gencanvas.core.ui.viewmodel.AuthenticatedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import com.lotusreichhart.gencanvas.core.common.R as CoreR
import com.lotusreichhart.gencanvas.core.model.banner.Banner
import com.lotusreichhart.gencanvas.feature.home.R
import com.lotusreichhart.gencanvas.feature.home.presentation.model.GridItemData
import com.lotusreichhart.gencanvas.feature.home.presentation.model.HorizontalItemData
import com.lotusreichhart.gencanvas.feature.home.presentation.model.MainListItemData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class HomeTabViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    globalUiEventManager: GlobalUiEventManager,
    getProfileStreamUseCase: GetProfileStreamUseCase,
    private val getListBannerUseCase: GetListBannerUseCase,
    private val refreshBannersUseCase: RefreshBannersUseCase,
) : AuthenticatedViewModel(
    networkMonitor = networkMonitor,
    globalUiEventManager = globalUiEventManager,
    getProfileStreamUseCase = getProfileStreamUseCase
) {

    private val defaultBanners = listOf(
        Banner(
            id = -1,
            title = "",
            imageUrl = R.drawable.default_banner_1.toString(),
            actionUrl = null,
            displayOrder = 1
        ),
        Banner(
            id = -2,
            title = "",
            imageUrl = R.drawable.default_banner_2.toString(),
            actionUrl = null,
            displayOrder = 2
        ),
        Banner(
            id = -3,
            title = "",
            imageUrl = R.drawable.default_banner_3.toString(),
            actionUrl = null,
            displayOrder = 3
        )
    )

    private val defaultGridItems = listOf(
        GridItemData(1, isLarge = true),
        GridItemData(2, isLarge = true),
        GridItemData(3, isLarge = false),
        GridItemData(4, isLarge = false),
        GridItemData(5, isLarge = false),
        GridItemData(6, isLarge = false)
    )
    private val defaultHorizontalListItems = (1..10).map { HorizontalItemData(it) }
    private val defaultMainListItems = (1..20).map { MainListItemData(it) }

    private val _uiState = MutableStateFlow(
        HomeTabUiState(
            isLoading = true,
            banners = defaultBanners,
            gridItems = defaultGridItems,
            horizontalListItems = defaultHorizontalListItems,
            mainListItems = defaultMainListItems
        )
    )

    val uiState: StateFlow<HomeTabUiState> = _uiState.asStateFlow()

    init {
        Timber.d("Chạy vào init")

        startBannerStreamListening()

        viewModelScope.launch {
            val bannerJob = async {
                try {
                    getListBannerUseCase().first()
                } catch (e: Exception) {
                    Timber.e("Lỗi lấy banner $e")
                    emptyList()
                }
            }

            delay(1500)
            val banners = bannerJob.await()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    banners = banners.ifEmpty { defaultBanners }
                )
            }
        }
    }

    fun onPullToRefresh() {
        Timber.d("onPullToRefresh....")
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            val result = refreshBannersUseCase()
            if (result.isFailure) {
                val exception = result.exceptionOrNull()
                Timber.e("result.isFailure: $exception")
                sendUiEvent(
                    UiEvent.ShowSnackBar(
                        message = exception?.asUiText() ?: TextResource.Id(CoreR.string.core_unknow_error),
                        type = UiEvent.SnackBarType.ERROR
                    )
                )
            }
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    private fun startBannerStreamListening() {
        getListBannerUseCase()
            .onEach { bannersFromStream ->
                val bannersToDisplay = bannersFromStream.ifEmpty {
                    defaultBanners
                }

                _uiState.update { currentState ->
                    currentState.copy(
                        error = null,
                        banners = bannersToDisplay,
                        gridItems = currentState.gridItems.ifEmpty { defaultGridItems },
                        horizontalListItems = currentState.horizontalListItems.ifEmpty { defaultHorizontalListItems },
                        mainListItems = currentState.mainListItems.ifEmpty { defaultMainListItems }
                    )
                }
            }
            .catch { e ->
                Timber.e("Lỗi stream banner: $e")
            }
            .launchIn(viewModelScope)
    }
}