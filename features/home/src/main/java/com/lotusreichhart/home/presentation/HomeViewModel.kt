package com.lotusreichhart.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lotusreichhart.core.ui.event.GlobalUiEventManager
import com.lotusreichhart.core.ui.event.UiEvent
import com.lotusreichhart.domain.entities.BannerEntity
import com.lotusreichhart.domain.monitor.NetworkMonitor
import com.lotusreichhart.domain.use_cases.banner.GetListBannerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import com.lotusreichhart.home.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    getListBannerUseCase: GetListBannerUseCase,
    networkMonitor: NetworkMonitor,
    private val globalUiEventManager: GlobalUiEventManager
) : ViewModel() {

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

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<HomeUiState> = networkMonitor.isOnline
        .flatMapLatest { isOnline ->
            if (isOnline) {
                getListBannerUseCase()
                    .map { serverBanners ->
                        val bannersToDisplay = serverBanners.ifEmpty {
                            defaultBanners
                        }
                        Log.d("HomeViewModel", "Có mạng, lấy banner THÀNH CÔNG.")

                        HomeUiState(
                            isLoading = false,
                            isOffline = false,
                            banners = bannersToDisplay,
                            gridItems = fakeGridItems,
                            horizontalListItems = fakeHorizontalListItems,
                            mainListItems = fakeMainListItems
                        )
                    }
                    .catch { exception ->
                        Log.e(
                            "HomeViewModel",
                            "Lỗi khi lấy banner (có mạng), dùng data dự phòng",
                            exception
                        )
                        emit(
                            HomeUiState(
                                isLoading = false,
                                isOffline = false,
                                banners = defaultBanners,
                                error = exception.localizedMessage,
                                gridItems = fakeGridItems,
                                horizontalListItems = fakeHorizontalListItems,
                                mainListItems = fakeMainListItems
                            )
                        )
                    }
            } else {
                Log.d("HomeViewModel", "Không có mạng, dùng data dự phòng")
                flowOf(
                    HomeUiState(
                        isLoading = false,
                        isOffline = true,
                        banners = defaultBanners,
                        gridItems = fakeGridItems,
                        horizontalListItems = fakeHorizontalListItems,
                        mainListItems = fakeMainListItems
                    )
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = HomeUiState(
                isLoading = true,
                banners = defaultBanners,
                gridItems = fakeGridItems,
                horizontalListItems = fakeHorizontalListItems,
                mainListItems = fakeMainListItems
            )
        )

    init {
        Log.d("HomeViewModel", "Chạy vào init")
        viewModelScope.launch {
            Log.d("HomeViewModel", "viewModelScope launch")

            var isFirstValue = true

            networkMonitor.isOnline.collect { isOnline ->
                if (isFirstValue) {
                    if (!isOnline) {
                        Log.d("HomeViewModel", "networkMonitor: Trạng thái ban đầu là Offline")
                        globalUiEventManager.showSnackBar(
                            "Không có kết nối internet",
                            type = UiEvent.SnackBarType.ERROR
                        )
                    } else {
                        Log.d("HomeViewModel", "networkMonitor: Trạng thái ban đầu là Online")
                    }
                    isFirstValue = false
                } else {
                    if (isOnline) {
                        Log.d("HomeViewModel", "networkMonitor: Đã khôi phục mạng")
                        globalUiEventManager.showSnackBar(
                            "Internet đã được khôi phục",
                            type = UiEvent.SnackBarType.SUCCESS
                        )
                    } else {
                        Log.d("HomeViewModel", "networkMonitor: Đã mất mạng")
                        globalUiEventManager.showSnackBar(
                            "Không có kết nối internet",
                            type = UiEvent.SnackBarType.ERROR
                        )
                    }
                }
            }
        }
    }
}