package com.lotusreichhart.home.presentation

import androidx.lifecycle.viewModelScope
import com.lotusreichhart.core.ui.event.GlobalUiEventManager
import com.lotusreichhart.core.ui.event.UiEvent
import com.lotusreichhart.core.utils.logD
import com.lotusreichhart.core.utils.logE
import com.lotusreichhart.core.viewmodel.BaseViewModel
import com.lotusreichhart.domain.entity.BannerEntity
import com.lotusreichhart.domain.monitor.NetworkMonitor
import com.lotusreichhart.domain.usecase.banner.GetListBannerUseCase
import com.lotusreichhart.domain.usecase.banner.RefreshBannersUseCase
import com.lotusreichhart.domain.usecase.user.GetProfileStreamUseCase
import com.lotusreichhart.home.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    networkMonitor: NetworkMonitor,
    globalUiEventManager: GlobalUiEventManager,
    private val getProfileStreamUseCase: GetProfileStreamUseCase,
    private val getListBannerUseCase: GetListBannerUseCase,
    private val refreshBannersUseCase: RefreshBannersUseCase,
) : BaseViewModel(
    networkMonitor = networkMonitor,
    globalUiEventManager = globalUiEventManager,
    getProfileStreamUseCase = getProfileStreamUseCase
) {

    private val defaultBanners = listOf(
        BannerEntity(
            id = -1,
            title = "",
            imageUrl = R.drawable.default_banner_1.toString(),
            actionUrl = null,
            displayOrder = 1
        ),
        BannerEntity(
            id = -2,
            title = "",
            imageUrl = R.drawable.default_banner_2.toString(),
            actionUrl = null,
            displayOrder = 2
        ),
        BannerEntity(
            id = -3,
            title = "",
            imageUrl = R.drawable.default_banner_3.toString(),
            actionUrl = null,
            displayOrder = 3
        )
    )

    private val fakeGridItems = listOf(
        GridItemData(1, isLarge = true),
        GridItemData(2, isLarge = true),
        GridItemData(3, isLarge = false),
        GridItemData(4, isLarge = false),
        GridItemData(5, isLarge = false),
        GridItemData(6, isLarge = false)
    )
    private val fakeHorizontalListItems = (1..10).map { HorizontalItemData(it) }
    private val fakeMainListItems = (1..20).map { MainListItemData(it) }

    val initialUser = currentUser.value
    private val _uiState = MutableStateFlow(
        HomeUiState(
            isLoading = true,
            userEntity = initialUser,
            banners = defaultBanners,
            gridItems = fakeGridItems,
            horizontalListItems = fakeHorizontalListItems,
            mainListItems = fakeMainListItems
        )
    )

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        logD("Chạy vào init")

        networkMonitor.isOnline
            .onEach { isOnline ->
                _uiState.update { it.copy(isOffline = !isOnline) }
            }
            .launchIn(viewModelScope)

        currentUser
            .onEach { user ->
                if (_uiState.value.userEntity != user) {
                    _uiState.update { it.copy(userEntity = user) }
                }
            }
            .launchIn(viewModelScope)

        startBannerStreamListening()

        viewModelScope.launch {
            val bannerJob = async {
                try {
                    getListBannerUseCase().first()
                } catch (e: Exception) {
                    emptyList()
                }
            }
            val userJob = async { getProfileStreamUseCase().first() }

            delay(1000)
            val banners = bannerJob.await()
            val user = userJob.await()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    userEntity = user,
                    banners = banners.ifEmpty { defaultBanners }
                )
            }
        }
    }

    fun onPullToRefresh() {
        logD("onPullToRefresh....")
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            val result = refreshBannersUseCase()
            if (result.isFailure) {
                val exception = result.exceptionOrNull()
                logE("result.isFailure", exception)
                sendUiEvent(
                    UiEvent.ShowSnackBar(
                        exception?.message ?: "Lỗi không xác định",
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
                        gridItems = currentState.gridItems.ifEmpty { fakeGridItems },
                        horizontalListItems = currentState.horizontalListItems.ifEmpty { fakeHorizontalListItems },
                        mainListItems = currentState.mainListItems.ifEmpty { fakeMainListItems }
                    )
                }
            }
            .catch { e ->
                logE("Lỗi stream banner nghiêm trọng", e)
            }
            .launchIn(viewModelScope)
    }
}